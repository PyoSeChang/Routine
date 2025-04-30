package com.routine.domain.c_routine.repository;

import java.util.List;

import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.model.RoutineTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineTaskRepository extends JpaRepository<RoutineTask, Long> {

    List<RoutineTask> findAllByRoutine(Routine routine);

    void deleteAllByRoutine(Routine original);

    List<RoutineTask> findAllByRoutineIdOrderByOrderNumberAsc(Long id);

    List<RoutineTask> findAllByRoutineId(Long id);
}
