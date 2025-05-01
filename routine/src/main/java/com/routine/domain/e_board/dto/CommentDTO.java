package com.routine.domain.e_board.dto;

import com.routine.domain.e_board.model.Comment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    private Long commentId;
    private Long boardId;
    private Long writerId;
    private String writerName;
    private String content;
    private Long parentId;
    private String createdAt;
    private String updatedAt;

    public static CommentDTO fromEntity(Comment c) {
        return CommentDTO.builder()
                .commentId(c.getId())
                .boardId(c.getBoard().getId())
                .writerId(c.getWriter().getId())
                .writerName(c.getWriter().getNickname())
                .content(c.getContent())
                .parentId(c.getParentId())
                .createdAt(c.getCreatedAt().toString())
                .updatedAt(c.getUpdatedAt() != null ? c.getUpdatedAt().toString() : null)
                .build();
    }
}
