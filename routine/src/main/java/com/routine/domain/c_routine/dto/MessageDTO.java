package com.routine.domain.c_routine.dto;

import com.routine.domain.d_routine_commit.model.CommitMessage;
import com.routine.domain.d_routine_commit.service.CommitMessageServiceImpl;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MessageDTO {
    private String message;
    private LocalDate commitDate;
    private int colorId;

    public static MessageDTO fromEntity(CommitMessage m) {
        return MessageDTO.builder()
                .message(m.getMessage())
                .commitDate(m.getCommitDate())
                .colorId(CommitMessageServiceImpl.generateColorIdExceptFor1())
                .build();
    }
}
