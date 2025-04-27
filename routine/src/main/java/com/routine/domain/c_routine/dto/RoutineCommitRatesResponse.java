package com.routine.domain.c_routine.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineCommitRatesResponse {

    private List<DailyRateDTO> thisWeekDailyRates;   // 이번 주 날짜별 루틴 이행률
    private List<DailyRateDTO> lastWeekDailyRates;    // 저번 주 날짜별 루틴 이행률
    private List<WeeklyRateDTO> routineWeeklyRates;   // 루틴 전체 주차 평균 이행률
    private List<TaskWeeklyRateDTO> taskWeeklyRates;  // 태스크별 주차 평균 이행률

}
