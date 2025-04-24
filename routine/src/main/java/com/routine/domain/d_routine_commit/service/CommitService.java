package com.routine.domain.d_routine_commit.service;

import com.routine.domain.d_routine_commit.dto.CommitRequestDTO;

public interface CommitService {

    void saveTodayCommitLog(Long memberId, CommitRequestDTO dto);

}
