package com.routine.domain.d_routine_commit.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitRequestDTO {

    private Long routineId;
    private boolean isSkipped;

    private List<TaskStatusDTO> taskStatuses; // ✅ 이걸 추가!

    private String message;
    private Boolean isPublic;

    @Getter @Setter
    public static class TaskStatusDTO {
        private Long taskId;
        private String status;
    }
}