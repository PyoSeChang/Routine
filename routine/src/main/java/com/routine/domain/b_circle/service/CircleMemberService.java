package com.routine.domain.b_circle.service;

public interface CircleMemberService {

    // 현재 skipCount 조회
    int getSkipCount(Long memberId, Long routineId);

    // skipCount 1 감소
    void decrementSkipCount(Long memberId, Long routineId);

    // 모든 skipCount 초기화
    void resetAllSkipCounts();

}
