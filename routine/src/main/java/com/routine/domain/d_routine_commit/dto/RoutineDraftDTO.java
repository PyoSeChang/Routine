package com.routine.domain.d_routine_commit.dto;

import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.model.RoutineTask;
import com.routine.domain.d_routine_commit.model.CommitDraft;
import com.routine.domain.d_routine_commit.model.enums.RoutineCommitStatus;
import com.routine.domain.d_routine_commit.model.TaskCheckStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RoutineDraftDTO {

    private Long routineId; // 루틴 ID
    private String title;
    private String category;
    private String detailCategory;
    private String tags;
    private String description;

    private List<TaskDTO> tasks; // ✅ Task 엔티티 기반 리스트
    private LocalDate draftDate;
    private RoutineCommitStatus status;

    @Getter
    @Setter
    public static class TaskDTO {
        private Long taskId;
        private String content;
        private boolean checked;

        public static TaskDTO from(RoutineTask task, List<TaskCheckStatus> checkedStatuses) {
            TaskDTO dto = new TaskDTO();
            dto.setTaskId(task.getId());
            dto.setContent(task.getContent());
            dto.setChecked(
                    checkedStatuses.stream()
                            .anyMatch(status -> status.getTaskId().equals(task.getId()) && status.isChecked())
            );
            return dto;
        }
    }

    // ✅ CommitDraft → DTO
    public static RoutineDraftDTO fromDraft(CommitDraft draft) {
        Routine routine = draft.getRoutine();
        List<TaskDTO> taskList = routine.getRoutineTasks().stream()
                .map(task -> TaskDTO.from(task, draft.getTaskStatuses()))
                .toList();

        RoutineDraftDTO dto = new RoutineDraftDTO();
        dto.setRoutineId(routine.getId());
        dto.setTitle(routine.getTitle());
        dto.setCategory(routine.getCategory());
        dto.setDetailCategory(routine.getDetailCategory());
        dto.setTags(routine.getTags());
        dto.setDescription(routine.getDescription());
        dto.setTasks(taskList);
        dto.setDraftDate(draft.getTargetDate());
        dto.setStatus(draft.getStatus());
        return dto;
    }

    // ✅ Routine → Draft용 초기 DTO
    public static RoutineDraftDTO fromRoutine(Routine routine, LocalDate today) {
        List<TaskDTO> taskList = routine.getRoutineTasks().stream()
                .map(task -> {
                    TaskDTO dto = new TaskDTO();
                    dto.setTaskId(task.getId());
                    dto.setContent(task.getContent());
                    dto.setChecked(false);
                    return dto;
                })
                .toList();

        RoutineDraftDTO dto = new RoutineDraftDTO();
        dto.setRoutineId(routine.getId());
        dto.setTitle(routine.getTitle());
        dto.setCategory(routine.getCategory());
        dto.setDetailCategory(routine.getDetailCategory());
        dto.setTags(routine.getTags());
        dto.setDescription(routine.getDescription());
        dto.setTasks(taskList);
        dto.setDraftDate(today);
        dto.setStatus(RoutineCommitStatus.DRAFT);
        return dto;
    }
}
