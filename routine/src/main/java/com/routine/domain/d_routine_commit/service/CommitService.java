package com.routine.domain.d_routine_commit.service;

import com.routine.domain.d_routine_commit.dto.CommitRequestDTO;

import java.time.LocalDate;

public interface CommitService {
    // 커밋하기
    void saveTodayCommitLog(Long memberId, CommitRequestDTO dto, LocalDate commitDate);

    // 커밋하지 않은 모든 NONE COMMITLOG들 FAIL 처리
    void autoFailUnsubmittedCommits(LocalDate targetDate);
}
