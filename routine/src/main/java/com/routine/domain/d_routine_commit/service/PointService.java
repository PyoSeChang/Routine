package com.routine.domain.d_routine_commit.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.d_routine_commit.model.enums.PointReason;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PointService {

    void rewardPoint(Member member, int amount, PointReason reason, Long routineId, LocalDate commitDate);

    void rewardForCircleRoutineCommit(Long memberId, Long routineId, LocalDate commitDate);

    // 자동 커밋에 대한 개인차원의 리워드를 전체적으로 지급
    void rewardCircleRoutineCommitToAll(LocalDate targetDate);

    // 서클 루틴 실행에 대한 서클차원의 리워드를 자격 있는 멤버들에 한해 지급
    void rewardCollectiveBonusForEligibleMembers(LocalDate targetDate);
}
