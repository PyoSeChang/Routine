package com.routine.domain.d_routine_commit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommitDraftForm {

    // 체크된 태스크 ID 리스트
    private List<Long> checkedTaskIds;

}