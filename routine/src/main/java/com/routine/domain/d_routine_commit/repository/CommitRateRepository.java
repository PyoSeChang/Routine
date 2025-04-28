package com.routine.domain.d_routine_commit.repository;



import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.d_routine_commit.model.CommitRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommitRateRepository extends JpaRepository<CommitRate, Long> {

    Optional<CommitRate> findByMemberIdAndRoutineIdAndYearAndWeek(Long memberId, Long routineId, int year, int week);

    Optional<CommitRate> findByMemberIdAndRoutineIdAndCommitDate(Long memberId, Long routineId, LocalDate commitDate);

    List<CommitRate> findAllByRoutineIdAndCommitDateBetween(Long routineId, LocalDate thisMonday, LocalDate thisSunday);

    @Query("""
    SELECT c FROM CommitRate c
    WHERE c.routine.id = :routineId
      AND c.commitDate IS NULL
      AND c.week IS NOT NULL
      AND c.year IS NOT NULL
      AND (c.year > :startYear
           OR (c.year = :startYear AND c.week >= :startWeek))
      AND (c.year < :endYear
           OR (c.year = :endYear AND c.week <= :endWeek))
    """)
    List<CommitRate> findWeeklyAverageCommitRatesByRoutineIdBetween(
            @Param("routineId") Long routineId,
            @Param("startYear") int startYear,
            @Param("startWeek") int startWeek,
            @Param("endYear") int endYear,
            @Param("endWeek") int endWeek
    );


    List<CommitRate> findAllByMemberIdInAndCommitDate(List<Long> memberIds, LocalDate commitDate);
}
