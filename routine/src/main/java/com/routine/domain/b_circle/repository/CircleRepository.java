package com.routine.domain.b_circle.repository;


import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.c_routine.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CircleRepository extends JpaRepository<Circle, Long> {

}
