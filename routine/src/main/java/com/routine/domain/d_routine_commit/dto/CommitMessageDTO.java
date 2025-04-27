package com.routine.domain.d_routine_commit.dto;

import com.routine.domain.d_routine_commit.model.CommitMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CommitMessageDTO {

    private String message;
    private LocalDate commitDate;

    public static CommitMessageDTO fromEntity(CommitMessage entity) {
        return new CommitMessageDTO(
                entity.getMessage(),
                entity.getCommitDate()
        );
    }
}
