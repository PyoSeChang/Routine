package com.routine.domain.d_routine_commit.controller;


import com.routine.domain.d_routine_commit.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommitController {

    private final CommitService commitService;


}
