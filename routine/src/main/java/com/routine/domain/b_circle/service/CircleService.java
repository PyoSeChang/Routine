package com.routine.domain.b_circle.service;

import com.routine.domain.b_circle.dto.CircleCreateRequest;
import com.routine.domain.b_circle.dto.CircleResponse;
import com.routine.domain.b_circle.dto.CircleSummary;
import com.routine.domain.b_circle.dto.RoutineSummaryDTO;
import com.routine.domain.b_circle.dto.CircleRoutineCommits;

import java.time.LocalDate;
import java.util.List;

public interface CircleService {

    List<CircleSummary> getMyCircles(Long memberId);


    Long createCircle(CircleCreateRequest request);

    CircleResponse getCircleDetail(Long circleId);

    List<RoutineSummaryDTO> getMyRoutinesForCircle(Long memberId, Long circleId);

    void convertRoutineToCircle(Long routineId, Long circleId, Long memberId);

    // 서클 페이지 갔을 때 멤버들의 오늘 루틴 실행 내역 출력하기
    CircleRoutineCommits getCommitsByCircleId(Long circleId, LocalDate commitDate);
}
