package com.routine.domain.d_routine_commit.repository;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.d_routine_commit.model.CommitDraft;
import com.routine.domain.d_routine_commit.model.enums.RoutineCommitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommitDraftRepository extends JpaRepository<CommitDraft, Long> {


    List<CommitDraft> findAllByMemberAndTargetDate(Member member, LocalDate targetDate);

    Optional<CommitDraft> findByMemberAndRoutineAndTargetDate(Member member, Routine routine, LocalDate targetDate);

    List<CommitDraft> findByTargetDateAndStatus(LocalDate targetDate, RoutineCommitStatus status);

    boolean existsByMemberAndRoutineAndTargetDate(Member member, Routine routine, LocalDate today);

    List<CommitDraft> findByTargetDate(LocalDate yesterday);
}
