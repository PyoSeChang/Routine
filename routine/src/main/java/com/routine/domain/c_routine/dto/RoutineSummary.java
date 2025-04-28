package com.routine.domain.c_routine.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoutineSummary {

    private Long routineId;
    private Long circleId;
    private String routineName;
    private List<DayOfWeek> repeatDays;
    private boolean circleRoutine;
    private LocalDate createdAt;

}
