package com.routine.domain.c_routine.service;

import com.routine.domain.c_routine.dto.RoutineDTO;
import com.routine.domain.c_routine.dto.RoutineSummary;
import com.routine.domain.c_routine.model.Routine;

import java.util.List;

public interface RoutineService {
    // 루틴의 기본적인 C, R, U, D
    void saveRoutine(RoutineDTO routineDTO, Long memberId, boolean isCircleRoutine);

    RoutineDTO getRoutine(Long routineId);

    void updateRoutine(RoutineDTO routineDTO, Long routineId);

    void deleteRoutine(Long routineId);

    List<RoutineDTO> getAllRoutinesByMember(Long memberId);

    // Commit Log 만들기 (View 출력용)
    void initializeTodayCommitLogs();

    // 나의 루틴 모아보기
    List<RoutineSummary> getMyRoutinesSummary(Long memberId);

    // 서클 루틴 만들기
    void saveCircleRoutine(Long memberId, Long CircleId);

}
