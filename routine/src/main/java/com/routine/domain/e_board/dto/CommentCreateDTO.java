// src/main/java/com/routine/domain/e_board/dto/CommentCreateDTO.java
package com.routine.domain.e_board.dto;

import lombok.Data;

/**
 * 댓글 등록 요청용 DTO
 */
@Data
public class CommentCreateDTO {
    private String content;
    private Long parentId; // null 이면 최상위 댓글
}