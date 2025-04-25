package com.routine.domain.d_routine_commit.controller;


import com.routine.domain.d_routine_commit.dto.CommitRequestDTO;
import com.routine.domain.d_routine_commit.service.CommitRateService;
import com.routine.domain.d_routine_commit.service.CommitService;
import com.routine.domain.d_routine_commit.service.PointService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/commit")
public class CommitController {

    private final CommitService commitService;
    private final CommitRateService commitRateService;
    private final PointService pointService;

    @PostMapping("/today")
    public ResponseEntity<Void> saveTodayCommitLog(@RequestBody CommitRequestDTO dto
//                                                   @AuthenticationPrincipal PrincipalDetails principal
    ) {
        // Long memberId = principal.getMember().getId();
        Long memberId = 1L;
        LocalDate commitDate = LocalDate.now();
        commitService.saveTodayCommitLog(memberId, dto, commitDate);
        double successRate = commitRateService.updateRecentRate(memberId, dto.getRoutineId(), commitDate);
        if (successRate >= 0.7) {
            pointService.rewardForCircleRoutineCommit(memberId, dto.getRoutineId(), commitDate);
        }
        return ResponseEntity.ok().build();
    }
}
