package com.routine.domain.c_routine.service;

import com.routine.domain.c_routine.dto.AllRoutinesViewDTO;
import com.routine.domain.c_routine.dto.RoutineCommitRatesResponse;
import com.routine.domain.c_routine.dto.RoutineDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoutineViewService {

    List<AllRoutinesViewDTO> getWeeklyRoutineView(Long memberId, LocalDate today);

    RoutineDTO getRoutineById(Long routineId);

    List<String> getCommitMessages(Long routineId);

    RoutineCommitRatesResponse getCommitRates(Long routineId);


}
