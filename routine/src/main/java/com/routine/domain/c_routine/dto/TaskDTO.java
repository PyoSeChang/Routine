package com.routine.domain.c_routine.dto;

import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import com.routine.domain.c_routine.model.RoutineTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TaskDTO {

    private Long taskId;
    private String content;
    private String status; // "SUCCESS", "FAIL", "SKIP", "NONE"
    private boolean checked; // status == SUCCESS 일 때 true

    public static TaskDTO from(CommitLog log) {
        CommitStatus status = log.getStatus();
        return TaskDTO.builder()
                .taskId(log.getTask().getId())
                .content(log.getTask().getContent())
                .status(status.name())
                .checked(status == CommitStatus.SUCCESS)
                .build();
    }

    public static TaskDTO fromTask(RoutineTask task) {
        return TaskDTO.builder()
                .taskId(task.getId())
                .content(task.getContent())
                .status("NONE")
                .checked(false)
                .build();
    }
}
