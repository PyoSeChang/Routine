package com.routine.domain.d_routine_commit.service.week;

import com.routine.domain.c_routine.dto.RoutineViewDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoutineViewService {

    // 최초 로그인 시 루틴 초안 만들기
    void initializeTodayDrafts(Long memberId);

    List<RoutineViewDTO> getWeeklyRoutineView(Long memberId, LocalDate today);
}
