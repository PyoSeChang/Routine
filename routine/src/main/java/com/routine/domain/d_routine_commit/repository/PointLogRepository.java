package com.routine.domain.d_routine_commit.repository;

import com.routine.domain.d_routine_commit.model.PointLog;
import com.routine.domain.d_routine_commit.model.enums.PointReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {
    // 중복 지급 방지
    boolean existsByMemberIdAndReasonAndDescription(Long memberId, PointReason reason, String description);

}
