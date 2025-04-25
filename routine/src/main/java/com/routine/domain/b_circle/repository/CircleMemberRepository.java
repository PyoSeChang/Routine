package com.routine.domain.b_circle.repository;


import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.b_circle.model.CircleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CircleMemberRepository extends JpaRepository<CircleMember, Long> {

    List<CircleMember> findAllByCircleId(Long circleId);
}
