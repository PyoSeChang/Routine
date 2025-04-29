package com.routine.domain.b_circle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoutineSummaryDTO {
    private Long routineId;
    private String title;
    private String category;
    private String detailCategory;
}
