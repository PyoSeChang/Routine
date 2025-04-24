package com.routine.domain.c_routine.dto;

import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.d_routine_commit.model.CommitDraft;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import com.routine.domain.d_routine_commit.model.enums.RoutineCommitStatus;
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
    private DayOfWeek weekday;              // 이 루틴이 속한 날짜의 요일
    private List<DayOfWeek> repeatDays;     // 반복 요일들 (루틴의 패턴)
    private LocalDate date;
    private WeekdayType type; // PAST, TODAY, UPCOMING
    private boolean isGroupRoutine;
    private boolean isSubmitted;
    private List<TaskDTO> tasks;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskDTO {
        private Long taskId;
        private String content;
        private CommitStatus status; // SUCCESS, FAIL, SKIP (과거)
        private boolean checked;
        // null (오늘 or 미래)
    }

    // ✅ PAST용 CommitLog 기반
    public static RoutineViewDTO fromCommitLogs(LocalDate date, List<CommitLog> logs) {
        Routine routine = logs.get(0).getRoutine(); // 동일 루틴 기준

        return RoutineViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .category(routine.getCategory())
                .detailCategory(routine.getDetailCategory())
                .description(routine.getDescription())
                .date(date)
                .weekday(date.getDayOfWeek())                    // 🔥 추가
                .repeatDays(routine.getRepeatDays())             // 🔥 추가
                .type(WeekdayType.PAST)
                .tasks(logs.stream()
                        .map(log -> TaskDTO.builder()
                                .taskId(log.getTaskId())
                                .content(null)
                                .status(log.getStatus())
                                .build())
                        .toList())
                .isGroupRoutine(routine.isGroupRoutine())
                .build();
    }

    // ✅ TODAY용 CommitDraft 기반
    public static RoutineViewDTO fromDraft(CommitDraft draft) {
        Routine routine = draft.getRoutine();

        return RoutineViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .category(routine.getCategory())
                .detailCategory(routine.getDetailCategory())
                .description(routine.getDescription())
                .date(draft.getTargetDate())
                .weekday(draft.getTargetDate().getDayOfWeek())   // 🔥 추가
                .repeatDays(routine.getRepeatDays())             // 🔥 추가
                .type(WeekdayType.TODAY)
                .isSubmitted(draft.getStatus() == RoutineCommitStatus.SUBMITTED)
                .tasks(draft.getTaskStatuses().stream()
                        .map(status -> TaskDTO.builder()
                                .taskId(status.getTaskId())
                                .content(status.getContent())
                                .status(null)
                                .checked(status.isChecked())
                                .build())
                        .toList())
                .isGroupRoutine(routine.isGroupRoutine())
                .build();
    }


    // ✅ UPCOMING용 RoutineTask 기반
    public static RoutineViewDTO fromRoutine(LocalDate futureDate, Routine routine) {
        return RoutineViewDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .category(routine.getCategory())
                .detailCategory(routine.getDetailCategory())
                .description(routine.getDescription())
                .date(futureDate)
                .weekday(futureDate.getDayOfWeek())              // 🔥 추가
                .repeatDays(routine.getRepeatDays())             // 🔥 추가
                .type(WeekdayType.UPCOMING)
                .tasks(routine.getRoutineTasks().stream()
                        .map(task -> TaskDTO.builder()
                                .taskId(task.getId())
                                .content(task.getContent())
                                .status(null)
                                .checked(false)
                                .build())
                        .toList())
                .isGroupRoutine(routine.isGroupRoutine())
                .build();
    }


}

