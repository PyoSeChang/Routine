package com.routine.domain.a_member.controller;

import com.routine.domain.a_member.dto.*;
import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.model.UserInfo;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.a_member.repository.UserInfoRepository;
import com.routine.domain.a_member.service.MemberService;
import com.routine.domain.c_routine.dto.RoutineSummary;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.domain.d_routine_commit.dto.PointLogDTO;
import com.routine.domain.d_routine_commit.service.PointService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final RoutineService routineService;
    private final MemberRepository memberRepository;
    private final PointService pointService;
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO dto) {
        memberService.register(dto);
        return ResponseEntity.ok().build();
    }

    // 아이디 중복 확인
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkLoginIdDuplicate(@RequestParam String loginId) {
        boolean isDuplicate = memberService.isLoginIdDuplicate(loginId);
        return ResponseEntity.ok(isDuplicate);
    }

    // 닉네임 중복 확인
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = memberService.isNicknameDuplicate(nickname);
        return ResponseEntity.ok(isDuplicate);
    }

    // My page
    @GetMapping("/{loginId}")
    public ResponseEntity<MyPageResponseDTO> getMyPageData(
            @PathVariable String loginId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        validateAccess(loginId, principalDetails);

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        UserInfo userInfo = userInfoRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        MyPageResponseDTO response = new MyPageResponseDTO(
                member.getId(),
                member.getPoint(),
                member.getNickname(),
                userInfo.getEmail()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{loginId}/points")
    @ResponseBody
    public ResponseEntity<List<PointLogDTO>> checkPoints(@PathVariable String loginId,
                                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        validateAccess(loginId, principalDetails);
        Long memberId = principalDetails.getMember().getId();
        List<PointLogDTO> pointLogs = pointService.showPointLogs(memberId);
        return ResponseEntity.ok(pointLogs);
    }

    @PostMapping("/{loginId}/check-password")
    public ResponseEntity<Void> checkPassword(
            @PathVariable String loginId,
            @RequestBody PasswordCheckDTO dto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        validateAccess(loginId, principalDetails);

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{loginId}/update")
    public ResponseEntity<Void> updateUserInfo(@PathVariable String loginId,
                                               @RequestBody UserInfoUpdateRequest request,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        validateAccess(loginId, principalDetails);
        memberService.userInfoUpdate(loginId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/points/{loginId}")
    public ResponseEntity<List<AdminPointLogDTO>> getPointLogsByLoginId(@PathVariable String loginId) {
        List<AdminPointLogDTO> pointLogs = memberService.getPointLogsByLoginId(loginId);
        return ResponseEntity.ok(pointLogs);
    }







    private void validateAccess(Long pathMemberId, PrincipalDetails principalDetails) {
        Long loggedInMemberId = principalDetails.getMember().getId();
        if (!loggedInMemberId.equals(pathMemberId)) {
            throw new AccessDeniedException("본인만 접근할 수 있습니다.");
        }
    }
    private void validateAccess(String pathLoginId, PrincipalDetails principalDetails) {
        String loggedInLoginId = principalDetails.getUsername();
        System.out.println("로그인한 로그인 아이디 :"+loggedInLoginId);
        System.out.println("경로로 온 아이디:"+pathLoginId);
        if (!loggedInLoginId.equals(pathLoginId)) {
            throw new AccessDeniedException("본인만 접근할 수 있습니다.");
        }
    }
}
