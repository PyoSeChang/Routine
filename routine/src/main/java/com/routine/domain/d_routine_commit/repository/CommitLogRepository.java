package com.routine.domain.d_routine_commit.repository;


import com.routine.domain.d_routine_commit.model.CommitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommitLogRepository extends JpaRepository<CommitLog, Long> {

    List<CommitLog> findAllByMemberIdAndCommitDate(Long memberId, LocalDate date);
}
