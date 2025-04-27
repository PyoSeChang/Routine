package com.routine.domain.c_routine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRateDTO {

    private String date;       // "2025-04-27" 형식 (문자열)
    private double commitRate; // 0.0 ~ 1.0 성공률

}
