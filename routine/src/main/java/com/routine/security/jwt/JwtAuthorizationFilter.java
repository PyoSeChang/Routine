package com.routine.security.jwt;

import com.routine.security.model.PrincipalDetails;
import com.routine.security.service.PrincipalDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService userDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtProvider jwtProvider,
                                  PrincipalDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("🌀 [1] JwtAuthorizationFilter 시작");

        // 1. 헤더 또는 쿠키에서 토큰 추출
        String token = resolveToken(request);
        if (token == null) {
            System.out.println("⛔ [중단] 토큰 없음 → 인증 없이 진행");
            chain.doFilter(request, response);
            return;
        }

        // 2. 유효성 검사
        try {
            if (!jwtProvider.isValid(token)) {
                System.out.println("⛔ [중단] 토큰 유효하지 않음");
                chain.doFilter(request, response);
                return;
            }
            System.out.println("✅ [2] 토큰 유효함");
        } catch (Exception e) {
            System.out.println("💥 [에러] 토큰 유효성 검사 중 예외: " + e.getMessage());
            chain.doFilter(request, response);
            return;
        }

        // 3. 토큰에서 사용자 정보 추출
        String loginId = null;
        try {
            loginId = jwtProvider.getUsernameFromToken(token);
            System.out.println("✅ [3] 토큰에서 loginId 추출됨: " + loginId);
        } catch (Exception e) {
            System.out.println("💥 [에러] 토큰에서 loginId 추출 실패: " + e.getMessage());
            chain.doFilter(request, response);
            return;
        }

        // 4. UserDetailsService 통해 사용자 조회
        PrincipalDetails principalDetails = null;
        try {
            principalDetails = (PrincipalDetails) userDetailsService.loadUserByUsername(loginId);
            System.out.println("✅ [4] principalDetails 조회됨: " + principalDetails.getMember().getLoginId());
        } catch (Exception e) {
            System.out.println("💥 [에러] principalDetails 조회 실패: " + e.getMessage());
            chain.doFilter(request, response);
            return;
        }

        // 5. 인증 객체 생성 및 SecurityContext에 등록
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("🎯 [5] SecurityContext에 인증 객체 등록 완료");
        System.out.println("🎯 인증된 사용자: " + principalDetails.getUsername());
        System.out.println("🎯 권한 목록: " + authentication.getAuthorities());

        // 6. 필터 체인 계속 진행
        chain.doFilter(request, response);
    }

    // 🔍 헤더 → 쿠키 순으로 accessToken 추출
    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("🌀 헤더에서 토큰 추출됨: " + token);
            return token;
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    System.out.println("🌀 쿠키에서 accessToken 추출됨: " + token);
                    return token;
                }
            }
        }

        return null;
    }


}
