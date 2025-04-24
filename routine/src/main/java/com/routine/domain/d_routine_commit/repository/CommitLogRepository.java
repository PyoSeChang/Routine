package com.routine.domain.d_routine_commit.repository;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommitLogRepository extends JpaRepository<CommitLog, Long> {

    boolean existsByRoutineAndMemberAndCommitDate(Routine routine, Member member, LocalDate commitDate);

    List<CommitLog> findAllByMemberIdAndCommitDate(Long memberId, LocalDate date);


    // 루틴 커밋 성공률 계산
    int countByMemberIdAndRoutineId(Long memberId, Long routineId);
    int countByMemberIdAndRoutineIdAndStatus(Long memberId, Long routineId, CommitStatus status);

    List<CommitLog> findByCommitDate(LocalDate today);

    List<CommitLog> findAllByMemberIdAndRoutineIdAndCommitDate(Long memberId, Long routineId, LocalDate today);
}
