package com.routine.domain.c_routine.dto;

import com.routine.domain.d_routine_commit.model.CommitRate;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyRateDTO {

    private String week;         // 사용자 친화적인 주차 표시 ("4월 2주차")
    private double commitRate;   // 0.0 ~ 1.0

    public static WeeklyRateDTO from(CommitRate rate) {
        String weekLabel = formatWeek(rate.getYear(), rate.getWeek());
        return WeeklyRateDTO.builder()
                .week(weekLabel)
                .commitRate(rate.getCommitRate())
                .build();
    }

    private static String formatWeek(int year, int week) {
        LocalDate firstDayOfWeek = LocalDate
                .ofYearDay(year, 1)
                .with(WeekFields.ISO.weekOfYear(), week)
                .with(DayOfWeek.MONDAY);

        Month month = firstDayOfWeek.getMonth();
        int monthValue = month.getValue();
        int weekInMonth = (firstDayOfWeek.getDayOfMonth() - 1) / 7 + 1;

        return String.format("%d월 %d주차", monthValue, weekInMonth);
    }


}


