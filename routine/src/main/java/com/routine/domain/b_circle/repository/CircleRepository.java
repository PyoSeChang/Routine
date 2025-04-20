package com.routine.domain.b_circle.repository;


import com.routine.domain.b_circle.model.Circle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CircleRepository extends JpaRepository<Circle, Long> {

}
