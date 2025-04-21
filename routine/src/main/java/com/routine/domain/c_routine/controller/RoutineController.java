package com.routine.domain.c_routine.controller;


import com.routine.domain.c_routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;


}
