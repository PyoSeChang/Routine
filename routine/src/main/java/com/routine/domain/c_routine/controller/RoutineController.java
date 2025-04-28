package com.routine.domain.c_routine.controller;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.c_routine.dto.RoutineDTO;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.domain.c_routine.dto.AllRoutinesViewDTO;
import com.routine.domain.d_routine_commit.service.CommitService;
import com.routine.domain.c_routine.service.RoutineViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/api/routine")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;
    private final CommitService commitService;
    private final MemberRepository memberRepository;
    private final RoutineViewService routineViewService;


    @PostMapping("/new-routine")
    public ResponseEntity<Void> createRoutine(@RequestBody RoutineDTO dto,
                                              @AuthenticationPrincipal Member member
    ) {
        Long memberId = member.getId();
        boolean isCircleRoutine = dto.isCircleRoutine();
        routineService.saveRoutine(dto, memberId, isCircleRoutine);
        return ResponseEntity.ok().build();
    }


}
