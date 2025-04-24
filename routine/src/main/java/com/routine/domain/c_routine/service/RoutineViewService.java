package com.routine.domain.c_routine.service;

import com.routine.domain.c_routine.dto.RoutineViewDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoutineViewService {

    List<RoutineViewDTO> getWeeklyRoutineView(Long memberId, LocalDate today);
}
