package com.routine.domain.b_circle.service;

import com.routine.domain.a_member.model.Member;

public interface CircleMemberService {

    // 현재 skipCount 조회
    int getSkipCount(Long memberId, Long routineId);

    // skipCount 1 감소
    void decrementSkipCount(Long memberId, Long routineId);

    // 모든 skipCount 초기화
    void resetAllSkipCounts();

    void register(Long circleId, Member member);

    void assignLeader(Long circleId, Long memberId, Long id);

    void kickMember(Long circleId, Long memberId, Long id);

    void leaveCircle(Long circleId, Long memberId, Long id);
}
