package com.routine.domain.e_board.dto;

import com.routine.domain.e_board.model.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class BoardDTO {
    private Long writer;
    private String writerNickname;
    private String title;
    private String content;
    private Long boardId;
    private String tags;
    private String category;
    private String detailCategory;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String boardType;
    private int viewCount;
    private List<CommentDTO> comments;

    public static BoardDTO fromEntity(Board board, BoardStatus status, List<CommentDTO> comments, String nickname) {
        return BoardDTO.builder()
                .boardId(board.getId())
                .writer(board.getWriter().getId())
                .writerNickname(nickname)
                .title(board.getTitle())
                .content(board.getContent())
                .tags(board.getTags())
                .category(board.getCategory().getDisplayName())
                .detailCategory(board.getDetailCategory().getDisplayName())
                .boardType(board.getType().getDisplayName())
                .createdAt(status.getCreatedAt())
                .viewCount(status.getViewCount())
                .updatedAt(status.getUpdatedAt())
                .comments(comments) // ✅ 댓글 포함
                .build();
    }
}
