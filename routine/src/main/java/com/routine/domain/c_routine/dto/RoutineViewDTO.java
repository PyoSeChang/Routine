package com.routine.domain.c_routine.dto;

import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import com.routine.domain.d_routine_commit.model.week.WeekdayType;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineViewDTO {

    private Long routineId;
    private String title;
    private String category;
    private String detailCategory;
    private String description;
    private DayOfWeek weekday;
    private List<DayOfWeek> repeatDays;
    private LocalDate date;
    private WeekdayType type;
    private boolean isGroupRoutine;
    private List<TaskDTO> tasks;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskDTO {
        private Long taskId;
        private String content;
        private CommitStatus status;
        private boolean checked;
    }

    private static RoutineViewDTOBuilder baseBuilder(Routine routine, LocalDate date, WeekdayType type) {
        return RoutineViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .category(routine.getCategory())
                .detailCategory(routine.getDetailCategory())
                .description(routine.getDescription())
                .date(date)
                .weekday(date.getDayOfWeek())
                .repeatDays(routine.getRepeatDays())
                .type(type)
                .isGroupRoutine(routine.isGroupRoutine());
    }

    public static RoutineViewDTO fromPastLogs(LocalDate date, List<CommitLog> logs) {
        Routine routine = logs.get(0).getRoutine();

        List<TaskDTO> taskDTOs = logs.stream()
                .map(log -> TaskDTO.builder()
                        .taskId(log.getTask().getId())
                        .content(log.getTask().getContent())
                        .status(log.getStatus())
                        .checked(false)
                        .build())
                .toList();

        return baseBuilder(routine, date, WeekdayType.PAST)
                .tasks(taskDTOs)
                .build();
    }

    public static RoutineViewDTO fromTodayLogs(LocalDate date, Routine routine, List<CommitLog> logs) {
        List<TaskDTO> taskDTOs = logs.stream()
                .map(log -> TaskDTO.builder()
                        .taskId(log.getTask().getId())
                        .content(log.getTask().getContent())
                        .status(null)
                        .checked(log.getStatus() == CommitStatus.SUCCESS)
                        .build())
                .toList();

        return baseBuilder(routine, date, WeekdayType.TODAY)
                .tasks(taskDTOs)
                .build();
    }

    public static RoutineViewDTO fromUpcoming(LocalDate futureDate, Routine routine) {
        List<TaskDTO> taskDTOs = routine.getRoutineTasks().stream()
                .map(task -> TaskDTO.builder()
                        .taskId(task.getId())
                        .content(task.getContent())
                        .status(null)
                        .checked(false)
                        .build())
                .toList();

        return baseBuilder(routine, futureDate, WeekdayType.UPCOMING)
                .tasks(taskDTOs)
                .build();
    }
}