package com.routine.domain.a_member.controller;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.c_routine.dto.RoutineSummary;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.domain.d_routine_commit.dto.PointLogDTO;
import com.routine.domain.d_routine_commit.service.PointService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class MyPageController {

    private final PointService pointService;
    private final RoutineService routineService;
    private final MemberRepository memberRepository;

    @GetMapping("/{memberId}")
    public String goToMyPage(@PathVariable Long memberId,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model) {
        validateAccess(memberId, principalDetails);
        model.addAttribute("memberId", memberId);
        List<RoutineSummary> routineSummaries = routineService.getMyRoutinesSummary(memberId);
        model.addAttribute("routineSummaries", routineSummaries);
        Member member = memberRepository.findById(memberId).get();
        int myPoints=member.getPoint();
        model.addAttribute("myPoints", myPoints);
        return "view/member/myPage"; // 변경된 경로
    }

    @GetMapping("/{memberId}/points")
    @ResponseBody
    public ResponseEntity<List<PointLogDTO>> checkPoints(@PathVariable Long memberId,
                                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        validateAccess(memberId, principalDetails);

        List<PointLogDTO> pointLogs = pointService.showPointLogs(memberId);
        return ResponseEntity.ok(pointLogs);
    }

    @GetMapping("/{memberId}/edit-userinfo")
    public String goToEditUserinfo(@PathVariable Long memberId,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails,
                                   Model model) {
        validateAccess(memberId, principalDetails);

        model.addAttribute("memberId", memberId);
        return "view/member/editUserinfo"; // 변경된 경로
    }

    @PostMapping("/{memberId}/edit-userinfo")
    public String editUserinfo(@PathVariable Long memberId,
                               @AuthenticationPrincipal PrincipalDetails principalDetails
            /*, @ModelAttribute EditUserinfoForm form */) {
        validateAccess(memberId, principalDetails);


        return "redirect:/member/myPage/" + memberId;
    }

    private void validateAccess(Long pathMemberId, PrincipalDetails principalDetails) {
        Long loggedInMemberId = principalDetails.getMember().getId();
        if (!loggedInMemberId.equals(pathMemberId)) {
            throw new AccessDeniedException("본인만 접근할 수 있습니다.");
        }
    }
}
