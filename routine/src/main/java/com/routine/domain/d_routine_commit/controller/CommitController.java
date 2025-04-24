package com.routine.domain.d_routine_commit.controller;


import com.routine.domain.d_routine_commit.dto.CommitRequestDTO;
import com.routine.domain.d_routine_commit.service.CommitService;
import com.routine.domain.d_routine_commit.service.PointService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/commit")
public class CommitController {

    private final CommitService commitService;

    @PostMapping("/today")
    public ResponseEntity<Void> saveTodayCommitLog(@RequestBody CommitRequestDTO dto
//                                                   @AuthenticationPrincipal PrincipalDetails principal
    ) {
        // Long memberId = principal.getMember().getId();
        Long memberId = 1L;
        commitService.saveTodayCommitLog(memberId, dto);
        return ResponseEntity.ok().build();
    }
}
