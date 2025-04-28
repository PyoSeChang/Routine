package com.routine.domain.e_board.dto;

import com.routine.domain.e_board.model.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class BoardDTO {
    private Long writer;
    private String title;
    private String content;
    private Long boardId;
    private String tags;
    private Category category;
    private DetailCategory detailCategory;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private BoardType boardType;
    private int viewCount;

    public static BoardDTO fromEntity(Board board, BoardStatus status) {

        return BoardDTO.builder()
                .boardId(board.getId())
                .writer(board.getWriter().getId())
                .title(board.getTitle())
                .content(board.getContent())
                .tags(board.getTags())
                .category(board.getCategory())
                .detailCategory(board.getDetailCategory())
                .boardType(board.getType())
                .createdAt(status.getCreatedAt())
                .viewCount(status.getViewCount())
                .updatedAt(status.getUpdatedAt())
                .build();
    }
}
