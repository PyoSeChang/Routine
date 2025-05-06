package com.routine.domain.c_routine.service;

import com.routine.domain.b_circle.repository.CircleMemberRepository;
import com.routine.domain.b_circle.service.CircleMemberService;
import com.routine.domain.c_routine.dto.*;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.model.RoutineTask;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.c_routine.repository.RoutineTaskRepository;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.c_routine.model.week.WeekdayVO;
import com.routine.domain.d_routine_commit.model.CommitRate;
import com.routine.domain.d_routine_commit.model.TaskCommitRate;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import com.routine.domain.d_routine_commit.repository.CommitMessageRepository;
import com.routine.domain.d_routine_commit.repository.CommitRateRepository;
import com.routine.domain.d_routine_commit.repository.TaskCommitRateRepository;
import com.routine.domain.d_routine_commit.service.CommitMessageService;
import com.routine.domain.d_routine_commit.service.week.WeekService;
import com.routine.domain.c_routine.model.week.WeekdayType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineViewServiceImpl implements RoutineViewService {

    private final RoutineRepository routineRepository;
    private final WeekService weekService;
    private final CommitLogRepository commitLogRepository;
    private final RoutineTaskRepository routineTaskRepository;
    private final CommitRateRepository commitRateRepository;
    private final TaskCommitRateRepository taskCommitRateRepository;
    private final CommitMessageService commitMessageService;
    private final CircleMemberService circleMemberService;
    private final CircleMemberRepository circleMemberRepository;
    private final CommitMessageRepository commitMessageRepository;

    @Override
    public List<AllRoutinesViewDTO> getWeeklyRoutineView(Long memberId, LocalDate today) {
        List<WeekdayVO> week = weekService.getThisWeek(today);
        List<AllRoutinesViewDTO> result = new ArrayList<>();

        for (WeekdayVO weekday : week) {
            LocalDate date = weekday.getDate();
            WeekdayType type = weekday.getType();
            result.addAll(getRoutineViews(memberId, date, type));
        }

        return result;
    }

    private List<AllRoutinesViewDTO> getRoutineViews(Long memberId, LocalDate date, WeekdayType type) {
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
                                yield AllRoutinesViewDTO.fromUpcoming(date, routine)
                                        .toBuilder()
                                        .type("PAST")
                                        .build();
                            } else {
                                yield AllRoutinesViewDTO.fromPastLogs(date, routine, routineLogs);
                            }
                        }
                        case TODAY -> {
                            AllRoutinesViewDTO.AllRoutinesViewDTOBuilder builder = AllRoutinesViewDTO.fromTodayLogs(date, routine, routineLogs).toBuilder();

                            if (routine.isGroupRoutine()) {
                                int skipCount = circleMemberService.getSkipCount(memberId, routine.getId());
                                builder.skipCount(skipCount);
                            }

                            yield builder.build();
                        }
                        case UPCOMING -> AllRoutinesViewDTO.fromUpcoming(date, routine);
                    };
                })
                .toList();
    }

    @Override
    public RoutineDTO getRoutineById(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 루틴입니다."));

        List<RoutineTask> routineTasks = routineTaskRepository.findAllByRoutine(routine);

        return RoutineDTO.fromEntity(routine, routineTasks);
    }

    @Override
    public RoutineCommitRatesResponse getCommitRates(Long routineId) {
        List<DailyRateDTO> thisWeekDailyRates = mapThisWeekDailyRates(routineId);
        List<DailyRateDTO> lastWeekDailyRates = mapLastWeekDailyRates(routineId);
        List<WeeklyRateDTO> routineWeeklyRates = mapRoutineWeeklyRates(routineId);
        List<TaskWeeklyRateDTO> taskWeeklyRates = mapTaskWeeklyRates(routineId);

        return RoutineCommitRatesResponse.builder()
                .thisWeekDailyRates(thisWeekDailyRates)
                .lastWeekDailyRates(lastWeekDailyRates)
                .routineWeeklyRates(routineWeeklyRates)
                .taskWeeklyRates(taskWeeklyRates)
                .build();
    }

    private List<DailyRateDTO> mapThisWeekDailyRates(Long routineId) {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);
        LocalDate thisSunday = thisMonday.plusDays(6);

        List<CommitRate> thisWeekRates = commitRateRepository.findAllByRoutineIdAndCommitDateBetween(routineId, thisMonday, thisSunday);

        return thisWeekRates.stream()
                .map(rate -> DailyRateDTO.builder()
                        .date(rate.getCommitDate().toString())
                        .commitRate(rate.getCommitRate())
                        .build())
                .collect(Collectors.toList());
    }

    private List<DailyRateDTO> mapLastWeekDailyRates(Long routineId) {
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);
        LocalDate lastMonday = thisMonday.minusWeeks(1);
        LocalDate lastSunday = lastMonday.plusDays(6);

        List<CommitRate> lastWeekRates = commitRateRepository.findAllByRoutineIdAndCommitDateBetween(routineId, lastMonday, lastSunday);

        return lastWeekRates.stream()
                .map(rate -> DailyRateDTO.builder()
                        .date(rate.getCommitDate().toString())
                        .commitRate(rate.getCommitRate())
                        .build())
                .collect(Collectors.toList());
    }

    private List<WeeklyRateDTO> mapRoutineWeeklyRates(Long routineId) {
        LocalDate today = LocalDate.now();
        List<Integer> recentWeeks = new ArrayList<>();
        List<Integer> recentYears = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            LocalDate target = today.minusWeeks(i);
            recentYears.add(target.getYear());
            recentWeeks.add(target.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        }

        List<CommitRate> rates = commitRateRepository.findAllByRoutineIdAndCommitDateIsNull(routineId);

        return rates.stream()
                .filter(rate -> recentYears.contains(rate.getYear()) && recentWeeks.contains(rate.getWeek()))
                .map(rate -> WeeklyRateDTO.builder()
                        .week(rate.getYear() + "-W" + rate.getWeek())
                        .commitRate(rate.getCommitRate())
                        .build())
                .collect(Collectors.toList());
    }

    private List<TaskWeeklyRateDTO> mapTaskWeeklyRates(Long routineId) {
        LocalDate today = LocalDate.now();
        Set<String> allowedWeeks = new HashSet<>();
        for (int i = 1; i <= 4; i++) {
            LocalDate target = today.minusWeeks(i);
            allowedWeeks.add(target.getYear() + "-W" + target.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        }

        List<TaskCommitRate> weeklyRates = taskCommitRateRepository.findAllByRoutineIdAndCommitDateIsNull(routineId);

        List<TaskCommitRate> filteredRates = weeklyRates.stream()
                .filter(rate -> allowedWeeks.contains(rate.getYear() + "-W" + rate.getWeek()))
                .toList();

        Map<Long, List<TaskCommitRate>> taskGrouped = filteredRates.stream()
                .collect(Collectors.groupingBy(TaskCommitRate::getTaskId));

        return taskGrouped.entrySet().stream()
                .map(entry -> {
                    Long taskId = entry.getKey();
                    List<TaskCommitRate> taskRates = entry.getValue();

                    String taskContent = routineTaskRepository.findById(taskId)
                            .map(RoutineTask::getContent)
                            .orElse("삭제된 태스크");

                    return TaskWeeklyRateDTO.builder()
                            .taskId(taskId)
                            .taskContent(taskContent)
                            .rates(taskRates.stream()
                                    .map(rate -> WeeklyRateDTO.builder()
                                            .week(rate.getYear() + "-W" + rate.getWeek())
                                            .commitRate(rate.getTaskCommitRate())
                                            .build())
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }
}