package com.routine.domain.c_routine.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskWeeklyRateDTO {

    private Long taskId;
    private String taskContent;
    private List<WeeklyRateDTO> rates; // 주차별 이행률
}
