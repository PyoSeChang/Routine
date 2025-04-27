package com.routine.domain.d_routine_commit.service;

import java.time.LocalDate;

public interface TaskCommitRateService {
    void saveWeeklyTaskCommitRates(LocalDate targetDate);
}
