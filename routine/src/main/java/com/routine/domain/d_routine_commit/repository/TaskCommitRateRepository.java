package com.routine.domain.d_routine_commit.repository;



import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.TaskCommitRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TaskCommitRateRepository extends JpaRepository<TaskCommitRate, Long> {


    List<TaskCommitRate> findAllByRoutineIdAndCommitDateBetween(Long routineId, LocalDate fiveWeeksAgoMonday, LocalDate lastSunday);
}
