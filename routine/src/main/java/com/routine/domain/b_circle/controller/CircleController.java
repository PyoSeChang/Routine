package com.routine.domain.b_circle.controller;


import com.routine.domain.b_circle.service.CircleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CircleController {

    private final CircleService circleService;


}
