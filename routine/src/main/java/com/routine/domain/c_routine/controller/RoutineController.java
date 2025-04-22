package com.routine.domain.c_routine.controller;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.c_routine.dto.RoutineDTO;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.domain.d_routine_commit.dto.RoutineDraftDTO;
import com.routine.domain.d_routine_commit.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/routine")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;
    private final CommitService commitService;
    private final MemberRepository memberRepository;


    @GetMapping("/goToRoutine")
    public String goToRoutinePage(Model model) {
        model.addAttribute("routine", new RoutineDTO());
        return "view/routine/routine";
    }

    @PostMapping("/savePersonalRoutine")
    public String savePersonalRoutine(RoutineDTO routineDTO) {
        Long memberId = 1L;
        boolean isCircleRoutine = false;
        routineService.saveRoutine(routineDTO, memberId, isCircleRoutine);
        return "redirect:/member/main";
    }

    @GetMapping("/myRoutines")
    public String displayAllMyRoutines(Model model) {
        Long memberId = 1L; // ✅ 로그인 구현 전이므로 하드코딩

        commitService.initializeTodayDrafts(memberId); // Draft 없으면 생성
        List<RoutineDraftDTO> todayDrafts = commitService.getTodayDrafts(memberId); // 모두 조회

        model.addAttribute("routines", todayDrafts);
        return "view/routine/my_routines";
    }


}
