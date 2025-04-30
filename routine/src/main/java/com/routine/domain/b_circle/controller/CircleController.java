package com.routine.domain.b_circle.controller;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.b_circle.dto.*;
import com.routine.domain.b_circle.service.CircleMemberService;
import com.routine.domain.b_circle.service.CircleService;
import com.routine.domain.c_routine.dto.RoutineDTO;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public List<CircleSummaryDTO> viewCirclePage(@AuthenticationPrincipal PrincipalDetails principal) {
        Long memberId = principal.getMember().getId();
        return circleService.getMyCircles(memberId);
    }

    //  2. 서클 생성
    @PostMapping
    public Long createCircle(@RequestBody CircleCreateRequest request,
                             @AuthenticationPrincipal PrincipalDetails principal) {
        Long memberId = principal.getMember().getId();
        Long circleId = circleService.createCircle(request, memberId);
        System.out.println("서클 이름 : " + request.getCircleName());
        System.out.println("서클 설명: "+request.getCircleDescription());
        System.out.println("서클 태그"+request.getTags());
        Long routineId = request.getRoutineId();
        routineService.saveAsCircleRoutine(memberId, routineId, circleId);
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

    @PostMapping("/create")
    public ResponseEntity<RoutineSummaryDTO> createRoutine(@RequestBody RoutineDTO dto,
                                              @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Long memberId = principal.getMember().getId();
        Routine newRoutine = routineService.saveRoutine(dto, memberId);
        return ResponseEntity.ok(new RoutineSummaryDTO(
                newRoutine.getId(),
                newRoutine.getTitle(),
                newRoutine.getCategory().name(),
                newRoutine.getDetailCategory().name()
        ));
    }

    // 6. 서클 가입
    @PostMapping("/{circleId}/join")
    public ResponseEntity<Void> joinCircle(@PathVariable Long circleId,
                                           @AuthenticationPrincipal PrincipalDetails principal) {
        Member member = principal.getMember();
        Long memberId = member.getId();
        circleMemberService.register(circleId, member);
        routineService.saveCircleRoutine(memberId, circleId);
        return ResponseEntity.ok().build();
    }

    // 7. 서클 검색
    @GetMapping("/search")
    public List<CircleSummaryDTO> searchCircles(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String detailCategory,
            @RequestParam(required = false) String keyword
    ) {
        return circleService.searchCircles(category, detailCategory, keyword);
    }


    // 8. 리더 위임
    @PutMapping("/{circleId}/members/{memberId}/assign-leader")
    public ResponseEntity<Void> assignLeader(
            @PathVariable Long circleId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        circleMemberService.assignLeader(circleId, memberId, principal.getMember().getId());
        return ResponseEntity.ok().build();
    }

    // 9. 추방
    @DeleteMapping("/{circleId}/members/{memberId}/remove")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long circleId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        circleMemberService.kickMember(circleId, memberId, principal.getMember().getId());
        return ResponseEntity.noContent().build();
    }

    // 10. 탈퇴
    @DeleteMapping("/{circleId}/members/{memberId}/leave")
    public ResponseEntity<Void> leaveCircle(
            @PathVariable Long circleId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        circleMemberService.leaveCircle(circleId, memberId, principal.getMember().getId());
        return ResponseEntity.noContent().build();
    }

}
