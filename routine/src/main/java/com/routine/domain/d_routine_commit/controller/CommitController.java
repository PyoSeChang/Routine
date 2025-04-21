package com.routine.domain.d_routine_commit.controller;


import com.routine.domain.d_routine_commit.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class CommitController {

    private final CommitService commitService;


}
