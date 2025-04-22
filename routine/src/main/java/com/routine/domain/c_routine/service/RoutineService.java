package com.routine.domain.c_routine.service;

import com.routine.domain.c_routine.dto.RoutineDTO;

import java.util.List;

public interface RoutineService {

    void saveRoutine(RoutineDTO routineDTO, Long memberId, boolean isCircleRoutine);

    RoutineDTO getRoutine(Long routineId);

    void updateRoutine(RoutineDTO routineDTO, Long routineId);

    void deleteRoutine(Long routineId);

    List<RoutineDTO> getAllRoutinesByMember(Long memberId);
}
