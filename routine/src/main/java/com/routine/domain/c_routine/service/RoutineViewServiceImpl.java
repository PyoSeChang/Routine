package com.routine.domain.c_routine.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.dto.RoutineViewDTO;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import com.routine.domain.d_routine_commit.model.week.WeekdayVO;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import com.routine.domain.d_routine_commit.service.week.WeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineViewServiceImpl implements RoutineViewService {

    private final RoutineRepository routineRepository;
    private final MemberRepository memberRepository;
    private final WeekService weekService;
    private final CommitLogRepository commitLogRepository;

    @Override
    public List<RoutineViewDTO> getWeeklyRoutineView(Long memberId, LocalDate today) {
        List<WeekdayVO> week = weekService.getThisWeek(today);
        List<RoutineViewDTO> result = new ArrayList<>();

        for (WeekdayVO weekday : week) {
            switch (weekday.getType()) {
                case PAST -> result.addAll(getPastRoutineView(memberId, weekday.getDate()));
                case TODAY -> result.addAll(getTodayRoutineView(memberId, weekday.getDate()));
                case UPCOMING -> result.addAll(getUpcomingRoutineView(memberId, weekday.getDate()));
            }
        }

        return result;
    }

    private List<RoutineViewDTO> getPastRoutineView(Long memberId, LocalDate date) {
        List<CommitLog> logs = commitLogRepository.findAllByMemberIdAndCommitDate(memberId, date);

        return logs.stream()
                .collect(Collectors.groupingBy(log -> log.getRoutine().getId()))
                .entrySet().stream()
                .map(entry -> RoutineViewDTO.fromPastLogs(date, entry.getValue()))
                .toList();
    }

    private List<RoutineViewDTO> getTodayRoutineView(Long memberId, LocalDate date) {
        List<CommitLog> logs = commitLogRepository.findAllByMemberIdAndCommitDate(memberId, date)
                .stream()
                .filter(log -> log.getStatus() == CommitStatus.NONE || log.getStatus() == CommitStatus.SUCCESS)
                .toList();

        Map<Long, List<CommitLog>> grouped = logs.stream()
                .collect(Collectors.groupingBy(log -> log.getRoutine().getId()));

        return grouped.entrySet().stream()
                .map(entry -> RoutineViewDTO.fromTodayLogs(date, entry.getValue().get(0).getRoutine(), entry.getValue()))
                .toList();
    }

    private List<RoutineViewDTO> getUpcomingRoutineView(Long memberId, LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        List<Routine> routines = routineRepository.findAllByMemberIdAndRepeatDaysContaining(memberId, dow);

        return routines.stream()
                .map(routine -> RoutineViewDTO.fromUpcoming(date, routine))
                .toList();
    }
}
