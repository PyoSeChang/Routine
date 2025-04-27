package com.routine.domain.d_routine_commit.repository;



import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.d_routine_commit.model.CommitRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommitRateRepository extends JpaRepository<CommitRate, Long> {

    Optional<CommitRate> findByMemberIdAndRoutineIdAndYearAndWeek(Long memberId, Long routineId, int year, int week);

    Optional<CommitRate> findByMemberIdAndRoutineIdAndCommitDate(Long memberId, Long routineId, LocalDate commitDate);

    List<CommitRate> findAllByRoutineIdAndCommitDateBetween(Long routineId, LocalDate thisMonday, LocalDate thisSunday);
}
