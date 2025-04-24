package com.routine.domain.c_routine.controller;

import com.routine.domain.c_routine.dto.RoutineViewDTO;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.domain.c_routine.service.RoutineViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineViewController {

    private final RoutineViewService routineViewService;
    private final RoutineService routineService;

    @GetMapping("/week")
    public ResponseEntity<List<RoutineViewDTO>> getWeeklyRoutines() {
        Long memberId = 1L; // ✅ 로그인 구현 전이므로 하드코딩
        LocalDate today = LocalDate.now();

        routineService.initializeTodayCommitLogs();
        // 전체 주간 루틴 반환
        List<RoutineViewDTO> routines = routineViewService.getWeeklyRoutineView(memberId, today);
        return ResponseEntity.ok(routines);
    }
}
