package com.routine.domain.c_routine.service;

import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.c_routine.dto.RoutineDTO;
import com.routine.domain.c_routine.dto.RoutineSummary;
import com.routine.domain.c_routine.model.Routine;

import java.util.List;

public interface RoutineService {
    // 루틴의 기본적인 C, R, U, D
    Routine saveRoutine(RoutineDTO routineDTO, Long memberId);

    RoutineDTO getRoutine(Long routineId);

    void updateRoutine(RoutineDTO routineDTO, Long routineId);

    void deleteRoutine(Long routineId);

    List<RoutineDTO> getAllRoutinesByMember(Long memberId);

    // Commit Log 만들기 (View 출력용)
    void initializeTodayCommitLogs();

    // 나의 루틴 모아보기
    List<RoutineSummary> getMyRoutinesSummary(Long memberId);

    // 서클 루틴 복사 및 저장
    void saveCircleRoutine(Long memberId, Long circleId);

    // 서클 루틴으로 만들기
    void saveAsCircleRoutine(Long memberId, Long routineId, Long circleId);

    // 개인 루틴으로 만들기
    void saveAsPersonalRoutine(Long memberId, Long circleId);

}
