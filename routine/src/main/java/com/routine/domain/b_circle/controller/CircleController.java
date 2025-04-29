package com.routine.domain.b_circle.controller;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.b_circle.dto.*;
import com.routine.domain.b_circle.service.CircleMemberService;
import com.routine.domain.b_circle.service.CircleService;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.domain.e_board.model.Category;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/circles")
@RequiredArgsConstructor
public class CircleController {

    private final CircleService circleService;
    private final CircleMemberService circleMemberService;
    private final RoutineService routineService;

    //  1. circle 기본 페이지
    @GetMapping
    public List<CircleSummary> getMyCircles(@AuthenticationPrincipal PrincipalDetails principal) {
        Long memberId = principal.getMember().getId();
        return circleService.getMyCircles(memberId);
    }

    //  2. 서클 생성
    @PostMapping
    public Long createCircle(@RequestBody CircleCreateRequest request,
                             @AuthenticationPrincipal PrincipalDetails principal) {
        Long memberId = principal.getMember().getId();
        Long circleId = circleService.createCircle(request, memberId);

        Long routineId = request.getRoutineId();
        routineService.saveCircleRoutine(memberId, routineId, circleId);
        return circleId;
    }

    //  3. 서클 상세 조회
    @GetMapping("/{circleId}")
    public CircleResponse getCircleDetail(@PathVariable Long circleId,
                                          @AuthenticationPrincipal PrincipalDetails principal) {
        Long memberId = principal.getMember().getId();
        return circleService.getCircleDetail(circleId, memberId);
    }

    // 4. 서클 루틴으로 만들 나의 루틴 불러오기
    @GetMapping("/my-routines")
    public List<RoutineSummaryDTO> getMyRoutinesForCircle(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam String detailCategory
    ) {
        Long memberId = principal.getMember().getId();
        return circleService.getMyRoutinesForCircle(memberId, detailCategory);
    }

    // 5. 4에서 선택한 나의 루틴 서클 루틴으로 변환
    @PostMapping("/{circleId}/convert-routine")
    public ResponseEntity<Void> convertRoutineToCircle(
            @PathVariable Long circleId,
            @RequestBody RoutineConvertRequest request,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Long memberId = principal.getMember().getId();
        circleService.convertRoutineToCircle(request.getRoutineId(), circleId, memberId);
        return ResponseEntity.ok().build();
    }

    // 6. 서클 가입
//    @PostMapping("/{circleId}/join")
//    public ResponseEntity<Void> joinCircle(@PathVariable Long circleId,
//                                           @AuthenticationPrincipal PrincipalDetails principal) {
//        Member member = principal.getMember();
//        Long memberId = member.getId();
//        circleMemberService.register(circleId, member);
//        routineService.saveCircleRoutine(memberId, circleId);
//        return ResponseEntity.ok().build();
//    }

}
