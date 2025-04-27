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
public class AllRoutinesViewDTO {

    private Long routineId;
    private String title;
    private String description;
    private String date;
    private String type;
    private DayOfWeek weekday;
    private List<DayOfWeek> repeatDays;
    private boolean isGroupRoutine;
    private boolean isRoutineSkipped;
    private List<TaskDTO> tasks;
    private int skipCount;

    public static AllRoutinesViewDTO fromPastLogs(LocalDate date, Routine routine, List<CommitLog> logs) {
        return AllRoutinesViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .date(date.toString())
                .type("PAST")
                .weekday(date.getDayOfWeek())
                .isGroupRoutine(routine.isGroupRoutine())
                .isRoutineSkipped(logs.stream().allMatch(log -> log.getStatus() == CommitStatus.SKIP))
                .tasks(logs.stream().map(TaskDTO::from).toList())
                .repeatDays(routine.getRepeatDays())
                .build();
    }

    public static AllRoutinesViewDTO fromTodayLogs(LocalDate date, Routine routine, List<CommitLog> logs) {
        return AllRoutinesViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .date(date.toString())
                .type("TODAY")
                .weekday(date.getDayOfWeek())
                .isGroupRoutine(routine.isGroupRoutine())
                .isRoutineSkipped(logs.stream().allMatch(log -> log.getStatus() == CommitStatus.SKIP))
                .tasks(logs.stream().map(TaskDTO::from).toList())
                .repeatDays(routine.getRepeatDays())
                .build();
    }

    public static AllRoutinesViewDTO fromUpcoming(LocalDate date, Routine routine) {
        List<TaskDTO> taskDTOs = routine.getRoutineTasks().stream()
                .map(TaskDTO::fromTask) // 🔥 RoutineTask → TaskDTO 변환 (status = NULL, checked = null 처리돼야 함)
                .collect(Collectors.toList());

        return AllRoutinesViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .date(date.toString())
                .type("UPCOMING")
                .weekday(date.getDayOfWeek())
                .isGroupRoutine(routine.isGroupRoutine())
                .isRoutineSkipped(false)
                .tasks(taskDTOs)
                .repeatDays(routine.getRepeatDays())
                .build();
    }
}
