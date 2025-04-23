package com.routine.domain.c_routine.repository;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.d_routine_commit.model.CommitDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findAllByMemberId(Long memberId);

    List<Routine> findAllByMemberAndRepeatDaysContaining(Member member, DayOfWeek repeatDay);

    List<Routine> findAllByMemberIdAndRepeatDaysContaining(Long memberId, DayOfWeek dow);
}
