package com.routine.domain.e_board.dto;

import com.routine.domain.e_board.model.Board;
import com.routine.domain.e_board.model.BoardStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder(toBuilder = true)
public class BoardListDTO {
    private Long boardId;
    private Long writerId;
    private String writerName;
    private String title;
    private String category;
    private String detailCategory;
    private String tags;
    private String modifiedDate;
    private int viewCount;
    private String boardType;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 엔티티 → DTO 변환
     */
    public static BoardListDTO fromEntity(Board board) {
        BoardStatus status = board.getStatus();
        System.out.println("status: " + status);
        LocalDateTime updatedAt = status != null ? status.getUpdatedAt() : null;
        System.out.println("updatedAt: " + updatedAt);
        String modifiedDate = "";
        if (updatedAt != null) {
            LocalDateTime now = LocalDateTime.now();
            modifiedDate = updatedAt.toLocalDate().isEqual(now.toLocalDate())
                    ? updatedAt.format(TIME_FORMAT)
                    : updatedAt.format(DATE_FORMAT);
        }

        return BoardListDTO.builder()
                .boardId(board.getId())
                .writerId(board.getWriter().getId())
                .writerName(board.getWriter().getNickname())
                .title(board.getTitle())
                .category(board.getDetailCategory().getParentCategory().getDisplayName())
                .detailCategory(board.getDetailCategory().getDisplayName())
                .tags(board.getTags())
                .modifiedDate(modifiedDate)
                .viewCount(status != null ? status.getViewCount() : 0)
                .boardType(board.getType().getDisplayName())
                .build();
    }
}
