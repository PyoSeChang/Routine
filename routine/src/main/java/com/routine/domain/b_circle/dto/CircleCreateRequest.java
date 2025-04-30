package com.routine.domain.b_circle.dto;

import com.routine.domain.e_board.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CircleCreateRequest {
    private String circleName;
    private Long routineId;
    private String circleDescription;
    private String tags;
    private String category;        // 대분류 (ex: LANGUAGE)
    private String detailCategory;  // 소분류 (ex: ENGLISH)
    private boolean opened;
    private int maxMemberCount;
}
