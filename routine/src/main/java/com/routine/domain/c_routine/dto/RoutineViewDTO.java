package com.routine.domain.c_routine.dto;

import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
public class RoutineViewDTO {

    private Long routineId;
    private String title;
    private String description;
    private String date;
    private String type;
    private DayOfWeek weekday;
    private boolean isGroupRoutine;
    private boolean isRoutineSkipped;
    private List<TaskDTO> tasks;

    public static RoutineViewDTO fromPastLogs(LocalDate date, Routine routine, List<CommitLog> logs) {
        return RoutineViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .date(date.toString())
                .type("PAST")
                .weekday(date.getDayOfWeek())
                .isGroupRoutine(routine.isGroupRoutine())
                .isRoutineSkipped(logs.stream().allMatch(log -> log.getStatus() == CommitStatus.SKIP))
                .tasks(logs.stream().map(TaskDTO::from).toList())
                .build();
    }

    public static RoutineViewDTO fromTodayLogs(LocalDate date, Routine routine, List<CommitLog> logs) {
        return RoutineViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .date(date.toString())
                .type("TODAY")
                .weekday(date.getDayOfWeek())
                .isGroupRoutine(routine.isGroupRoutine())
                .isRoutineSkipped(logs.stream().allMatch(log -> log.getStatus() == CommitStatus.SKIP))
                .tasks(logs.stream().map(TaskDTO::from).toList())
                .build();
    }

    public static RoutineViewDTO fromUpcoming(LocalDate date, Routine routine) {
        return RoutineViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .date(date.toString())
                .type("UPCOMING")
                .weekday(date.getDayOfWeek())
                .isGroupRoutine(routine.isGroupRoutine())
                .isRoutineSkipped(false)
                .tasks(routine.getRoutineTasks().stream().map(TaskDTO::fromTask).collect(Collectors.toList()))
                .build();
    }
}
