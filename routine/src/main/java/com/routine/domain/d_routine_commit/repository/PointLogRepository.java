package com.routine.domain.d_routine_commit.repository;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.d_routine_commit.model.PointLog;
import com.routine.domain.d_routine_commit.model.enums.PointLogStatus;
import com.routine.domain.d_routine_commit.model.enums.PointReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {
    // 중복 지급 방지
    boolean existsByMemberIdAndReasonAndRoutineIdAndCommitDate(Long memberId, PointReason pointReason, Long routineId, LocalDate commitDate);

    LocalDate commitDate(LocalDate commitDate);

    boolean existsByMemberIdAndReasonAndRoutineIdAndCreatedAtBetween(Long memberId, PointReason pointReason, Long routineId, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<PointLog> findAllByReasonAndCommitDate(PointReason pointReason, LocalDate targetDate);

    List<PointLog> findAllByMemberIdAndStatus(Long memberId, PointLogStatus pointLogStatus);

    List<PointLog> findByMember(Member member);
}
