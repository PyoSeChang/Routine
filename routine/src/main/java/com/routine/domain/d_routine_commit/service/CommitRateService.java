package com.routine.domain.d_routine_commit.service;

import java.time.LocalDate;

public interface CommitRateService {
    // 커밋 후 실행률 업데이트
    double updateRecentRate(Long memberId, Long routineId, LocalDate commitDate);
    // 스킵한 루틴에 대한 실행률 계산 제외
    void saveSkippedRate(Long memberId, Long routineId, LocalDate commitDate);
    // 자동 커밋 후 실행률 업데이트
    void updateRateByAutoCommit(LocalDate targetDate);
}
