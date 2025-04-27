package com.routine.domain.d_routine_commit.service.week;

import com.routine.domain.c_routine.model.week.WeekdayType;
import com.routine.domain.c_routine.model.week.WeekdayVO;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class WeekServiceImpl implements WeekService {
    @Override
    public LocalDate getStartOfWeek(LocalDate today) {
        return today.with(DayOfWeek.MONDAY); // ISO 기준 월요일 시작
    }

    @Override
    public List<WeekdayVO> getThisWeek(LocalDate today) {
        LocalDate startOfWeek = getStartOfWeek(today);

        return IntStream.range(0, 7)
                .mapToObj(i -> {
                    LocalDate date = startOfWeek.plusDays(i);
                    WeekdayType type = date.isBefore(today)
                            ? WeekdayType.PAST
                            : date.equals(today)
                            ? WeekdayType.TODAY
                            : WeekdayType.UPCOMING;

                    return new WeekdayVO(date, date.getDayOfWeek(), type);
                })
                .collect(Collectors.toList());
    }

    @Override
    public WeekdayVO getWeekdayInfo(LocalDate date, LocalDate today) {
        WeekdayType type;

        if (date.isBefore(today)) {
            type = WeekdayType.PAST;
        } else if (date.isEqual(today)) {
            type = WeekdayType.TODAY;
        } else {
            type = WeekdayType.UPCOMING;
        }

        return new WeekdayVO(date, date.getDayOfWeek(), type);
    }
}
