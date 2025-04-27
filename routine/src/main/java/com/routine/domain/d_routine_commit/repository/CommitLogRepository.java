package com.routine.domain.d_routine_commit.repository;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.model.RoutineTask;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommitLogRepository extends JpaRepository<CommitLog, Long> {

    List<CommitLog> findAllByMemberIdAndCommitDate(Long memberId, LocalDate date);

    List<CommitLog> findByCommitDate(LocalDate today);

    List<CommitLog> findAllByMemberIdAndRoutineIdAndCommitDate(Long memberId, Long routineId, LocalDate today);
    // Commit Log 중복 조회
    boolean existsByRoutineAndMemberAndTaskAndCommitDate(Routine routine, Member member, RoutineTask task, LocalDate today);

    List<CommitLog> findAllByCommitDateAndStatus(LocalDate today, CommitStatus commitStatus);

    List<Long> findRoutineIdsByCommitDate(LocalDate targetDate);

    List<CommitLog> findAllByRoutineIdAndCommitDateAndStatusNot(Long routineId, LocalDate targetDate, CommitStatus commitStatus);

    List<CommitLog> findAllByCommitDate(LocalDate targetDate);

    Optional<CommitLog> findByMemberIdAndRoutineCircleIdAndCommitDate(Long id, Long circleId, LocalDate targetDate);

    List<CommitLog> findByRoutineIdAndCommitDateBetween(Long id, LocalDate monday, LocalDate sunday);

    @Query("SELECT DISTINCT c.commitDate FROM CommitLog c WHERE c.member.id = :memberId AND c.routine.id = :routineId ORDER BY c.commitDate")
    List<LocalDate> findCommitDates(@Param("memberId") Long memberId, @Param("routineId") Long routineId);

}
