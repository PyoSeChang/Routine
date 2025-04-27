package com.routine.domain.c_routine.repository;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findAllByMemberId(Long memberId);

    List<Routine> findAllByMemberAndRepeatDaysContaining(Member member, DayOfWeek repeatDay);

    List<Routine> findAllByMemberIdAndRepeatDaysContaining(Long memberId, DayOfWeek dow);

    List<Routine> findAllByMemberAndRepeatDays(Member member, List<DayOfWeek> repeatDays);

    @Query("SELECT r.circle.id FROM Routine r WHERE r.id = :routineId")
    Optional<Long> findCircleIdById(@Param("routineId") Long routineId);

}
