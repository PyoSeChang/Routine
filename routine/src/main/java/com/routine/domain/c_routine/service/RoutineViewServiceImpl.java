package com.routine.domain.c_routine.service;

import com.routine.domain.c_routine.dto.RoutineViewDTO;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.week.WeekdayVO;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import com.routine.domain.d_routine_commit.service.week.WeekService;
import com.routine.domain.d_routine_commit.model.week.WeekdayType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineViewServiceImpl implements RoutineViewService {

    private final RoutineRepository routineRepository;
    private final WeekService weekService;
    private final CommitLogRepository commitLogRepository;

    @Override
    public List<RoutineViewDTO> getWeeklyRoutineView(Long memberId, LocalDate today) {
        List<WeekdayVO> week = weekService.getThisWeek(today);
        List<RoutineViewDTO> result = new ArrayList<>();

        for (WeekdayVO weekday : week) {
            LocalDate date = weekday.getDate();
            WeekdayType type = weekday.getType();
            result.addAll(getRoutineViews(memberId, date, type));
        }

        return result;
    }

    private List<RoutineViewDTO> getRoutineViews(Long memberId, LocalDate date, WeekdayType type) {
        DayOfWeek dow = date.getDayOfWeek();

        List<Routine> routines = routineRepository.findAllByMemberIdAndRepeatDaysContaining(memberId, dow);
        List<CommitLog> logs = (type == WeekdayType.UPCOMING) ? List.of()
                : commitLogRepository.findAllByMemberIdAndCommitDate(memberId, date);

        Map<Long, List<CommitLog>> logMap = logs.stream()
                .collect(Collectors.groupingBy(log -> log.getRoutine().getId()));

        return routines.stream()
                .map(routine -> {
                    List<CommitLog> routineLogs = logMap.getOrDefault(routine.getId(), List.of());
                    return switch (type) {
                        case PAST -> {
                            if (routineLogs.isEmpty()) {
                                yield RoutineViewDTO.fromUpcoming(date, routine)
                                        .toBuilder()
                                        .type("PAST")
                                        .build();
                            } else {
                                yield RoutineViewDTO.fromPastLogs(date, routine, routineLogs);
                            }
                        }
                        case TODAY -> RoutineViewDTO.fromTodayLogs(date, routine, routineLogs);
                        case UPCOMING -> RoutineViewDTO.fromUpcoming(date, routine);
                    };
                })
                .toList();
    }
}