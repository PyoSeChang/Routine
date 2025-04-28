// src/main/java/com/routine/domain/e_board/dto/CommentUpdateDTO.java
package com.routine.domain.e_board.dto;

import lombok.Data;

/**
 * 댓글 수정 요청용 DTO
 */
@Data
public class CommentUpdateDTO {
    private String content;
}