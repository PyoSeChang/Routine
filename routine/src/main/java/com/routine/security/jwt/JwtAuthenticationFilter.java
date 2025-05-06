package com.routine.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.routine.security.model.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginRequestDTO creds = mapper.readValue(request.getInputStream(), LoginRequestDTO.class);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(creds.getLoginId(), creds.getPassword());

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        String jwt = jwtProvider.createToken(principal.getUsername(), principal.getMember().getRole());

        response.addHeader("Authorization", "Bearer " + jwt);

        Cookie cookie = new Cookie("accessToken", jwt);
        cookie.setHttpOnly(true); // JS로 접근 못하게 (보안)
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 하루짜리

        response.addCookie(cookie);
    }

    static class LoginRequestDTO {
        private String loginId;
        private String password;

        public String getLoginId() { return loginId; }
        public String getPassword() { return password; }
        public void setLoginId(String loginId) { this.loginId = loginId; }
        public void setPassword(String password) { this.password = password; }
    }
}
