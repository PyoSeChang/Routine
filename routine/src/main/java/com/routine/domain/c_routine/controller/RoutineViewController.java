package com.routine.domain.c_routine.controller;

import com.routine.domain.d_routine_commit.dto.RoutineViewDTO;
import com.routine.domain.d_routine_commit.service.week.RoutineViewService;
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

    @GetMapping("/week")
    public ResponseEntity<List<RoutineViewDTO>> getWeeklyRoutines() {
        Long memberId = 1L; // ✅ 로그인 구현 전이므로 하드코딩
        LocalDate today = LocalDate.now();

        // 오늘 드래프트 먼저 생성
        routineViewService.initializeTodayDrafts(memberId);

        // 전체 주간 루틴 반환
        List<RoutineViewDTO> routines = routineViewService.getWeeklyRoutineView(memberId, today);
        return ResponseEntity.ok(routines);
    }
}
