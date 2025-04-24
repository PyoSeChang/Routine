package com.routine.domain.d_routine_commit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommitDraftForm {

    private List<TaskInput> tasks;
    private boolean isSkipped;
    private String isSkippedStr;

    @Getter
    @Setter
    public static class TaskInput {
        private Long taskId;
        private String content;
        private boolean checked;
    }
}