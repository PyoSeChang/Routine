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

    private List<Long> checkedTaskIds;

    private boolean isSkipped;
}

