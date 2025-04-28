package com.routine.domain.e_board.controller;

import com.routine.domain.e_board.dto.CommentCreateDTO;
import com.routine.domain.e_board.dto.CommentDTO;
import com.routine.domain.e_board.dto.CommentUpdateDTO;
import com.routine.domain.e_board.service.CommentService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long boardId,
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody CommentCreateDTO createDTO
    ) {
        Long userId = principal.getMember().getId();
        CommentDTO newComment = commentService.addComment(userId, boardId, createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> listComments(
            @PathVariable Long boardId
    ) {
        List<CommentDTO> comments = commentService.getComments(boardId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody CommentUpdateDTO updateDTO
    ) {
        Long userId = principal.getMember().getId();
        // 실제 작성자 검증은 서비스 레이어에서 수행
        CommentDTO updated = commentService.updateComment(userId, boardId, commentId, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Long userId = principal.getMember().getId();
        commentService.deleteComment(userId, boardId, commentId);
        return ResponseEntity.noContent().build();
    }

}
