package com.routine.domain.d_routine_commit.service.week;

import com.routine.domain.d_routine_commit.model.week.WeekdayVO;

import java.time.LocalDate;
import java.util.List;

public interface WeekService {
    List<WeekdayVO> getThisWeek(LocalDate today);
    WeekdayVO getWeekdayInfo(LocalDate date, LocalDate today);
    LocalDate getStartOfWeek(LocalDate today); // 월요일
}
