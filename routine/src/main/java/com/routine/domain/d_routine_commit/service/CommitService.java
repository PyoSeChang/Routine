package com.routine.domain.d_routine_commit.service;

import com.routine.domain.c_routine.dto.CommitLogDTO;
import com.routine.domain.d_routine_commit.dto.CommitRequestDTO;

import java.time.LocalDate;
import java.util.List;

public interface CommitService {
    // 커밋하기
    void saveTodayCommitLog(Long memberId, CommitRequestDTO dto, LocalDate commitDate);

    // 커밋 날짜 모아보기
    List<LocalDate> getCommitDatesByRoutineId(Long memberId, Long routineId);

    // 커밋 로그 모아보기
    CommitLogDTO getCommitLogSummary(Long memberId, Long routineId, LocalDate date);
}
