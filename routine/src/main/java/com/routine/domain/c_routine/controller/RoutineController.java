package com.routine.domain.c_routine.controller;


import com.routine.domain.c_routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;


}
