// src/main/java/com/routine/domain/e_board/dto/CommentDTO.java
package com.routine.domain.e_board.dto;

import com.routine.domain.e_board.model.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 댓글 응답용 DTO
 */
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

    @Builder.Default
    private List<CommentDTO> replies = new ArrayList<>();

    public static CommentDTO fromEntity(Comment c) {
        CommentDTO dto = CommentDTO.builder()
                .commentId(c.getId())
                .boardId(c.getBoard().getId())
                .writerId(c.getWriter().getId())
                .writerName(c.getWriter().getNickname())
                .content(c.getContent())
                .parentId(c.getParentId())
                .createdAt(c.getCreatedAt().toString())
                .updatedAt(c.getUpdatedAt() != null ? c.getUpdatedAt().toString() : null)
                .build();
        // replies는 기본 빈 리스트 상태로 둡니다.
        return dto;
    }
}






