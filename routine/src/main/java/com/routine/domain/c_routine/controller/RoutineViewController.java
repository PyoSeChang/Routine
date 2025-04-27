package com.routine.domain.c_routine.controller;

import com.routine.domain.c_routine.dto.AllRoutinesViewDTO;
import com.routine.domain.c_routine.dto.CommitLogDTO;
import com.routine.domain.c_routine.dto.RoutineCommitRatesResponse;
import com.routine.domain.c_routine.dto.RoutineDTO;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.domain.c_routine.service.RoutineViewService;
import com.routine.domain.d_routine_commit.service.CommitService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/routine")
@RequiredArgsConstructor
public class RoutineViewController {

    private final RoutineViewService routineViewService;
    private final RoutineService routineService;
    private final CommitService commitService;

    @GetMapping("/week")
    public ResponseEntity<List<AllRoutinesViewDTO>> getWeeklyRoutines() {
        Long memberId = 1L; // ✅ 로그인 구현 전이므로 하드코딩
        LocalDate today = LocalDate.now();

        routineService.initializeTodayCommitLogs();
        // 전체 주간 루틴 반환
        List<AllRoutinesViewDTO> routines = routineViewService.getWeeklyRoutineView(memberId, today);
        return ResponseEntity.ok(routines);
    }

    @GetMapping("/{routineId}")
    public ResponseEntity<RoutineDTO> getRoutine(@PathVariable Long routineId) {
        RoutineDTO routine = routineViewService.getRoutineById(routineId);
        return ResponseEntity.ok(routine);
    }

    @GetMapping("/{routineId}/messages")
    public ResponseEntity<List<String>> getRoutineMessages(@PathVariable Long routineId) {
        List<String> messages = routineViewService.getCommitMessages(routineId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{routineId}/rates")
    public ResponseEntity<RoutineCommitRatesResponse> getRoutineCommitRates(@PathVariable Long routineId) {
        RoutineCommitRatesResponse rates = routineViewService.getCommitRates(routineId);
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/{routineId}/commit-dates")
    public List<LocalDate> getCommitDates(
            @PathVariable Long routineId //,
            // @AuthenticationPrincipal PrincipalDetails principal
    ) {
        // Long memberId = principal.getMember().getId();
        Long memberId = 1L;
        return commitService.getCommitDatesByRoutineId(memberId, routineId);
    }


    @GetMapping("/{routineId}/commit")
    public CommitLogDTO getCommitsByRoutineAndDate(
            @PathVariable Long routineId,
            @RequestParam LocalDate date
            //@AuthenticationPrincipal PrincipalDetails principal
    ) {
        // Long memberId = principal.getMember().getId();
        Long memberId = 1L;
        return commitService.getCommitLogSummary(memberId, routineId, date);
    }

}
