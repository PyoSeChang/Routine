package com.routine.domain.b_circle.controller;


import com.routine.domain.b_circle.service.CircleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class CircleController {

    private final CircleService circleService;


}
