package com.routine.domain.c_routine.model.week;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class WeekdayVO {
    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private WeekdayType type; // enum: PAST, TODAY, UPCOMING
}