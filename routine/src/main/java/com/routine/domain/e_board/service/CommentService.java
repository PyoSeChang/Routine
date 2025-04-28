package com.routine.domain.e_board.service;

import com.routine.domain.e_board.dto.CommentCreateDTO;
import com.routine.domain.e_board.dto.CommentDTO;
import com.routine.domain.e_board.dto.CommentUpdateDTO;

import java.util.List;

public interface CommentService {


    CommentDTO addComment(Long userId, Long boardId, CommentCreateDTO createDTO);

    CommentDTO updateComment(Long userId, Long boardId, Long commentId, CommentUpdateDTO updateDTO);

    void deleteComment(Long userId, Long boardId, Long commentId);

    List<CommentDTO> getComments(Long boardId);
}
