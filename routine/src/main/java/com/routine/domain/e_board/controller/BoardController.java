package com.routine.domain.e_board.controller;

import com.routine.domain.e_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


}
