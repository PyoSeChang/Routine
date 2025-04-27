package com.routine.domain.c_routine.scheduler;


import com.routine.domain.b_circle.service.CircleMemberService;
import com.routine.domain.c_routine.service.RoutineService;
import com.routine.domain.d_routine_commit.service.CommitRateService;
import com.routine.domain.d_routine_commit.service.CommitService;
import com.routine.domain.d_routine_commit.service.PointService;
import com.routine.domain.d_routine_commit.service.TaskCommitRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Transactional
public class RoutineScheduler {

    private final CommitService commitService;
    private final PointService pointService;
    private final CommitRateService commitRateService;
    private final RoutineService routineService;
    private final TaskCommitRateService taskCommitRateService;
    private final CircleMemberService circleMemberService;

    @Scheduled( cron = "0 0 0 * * *")
    public void initializeCommitLogsForToday() {
        routineService.initializeTodayCommitLogs();
    }

    @Scheduled( cron = "0 0 0 * * *")
    public void autoSaveCommitRatesAndRewardingPoints() {
        LocalDate targetDate = LocalDate.now().minusDays(1);
        commitRateService.updateRateByAutoCommit(targetDate);
        pointService.rewardCircleRoutineCommitToAll(targetDate); // Circle Routine 이행에 대한 포인트 조건부 지급
        pointService.rewardCollectiveBonusForEligibleMembers(targetDate); // Circle에 포함된 멤버들의 참여율에 따른 포인트 조건부 지급
    }
    @Scheduled( cron = "0 0 0 * * MON")
    public void autoSaveWeeklyCommitRatesPerTasks() {
        LocalDate targetDate = LocalDate.now().minusDays(1);
        taskCommitRateService.saveWeeklyTaskCommitRates(targetDate);
    }


    @Scheduled(cron = "0 0 0 1 * ?")  // 매월 1일 00:00에 실행
    public void resetAllSkipCounts() {
        circleMemberService.resetAllSkipCounts();
    }

}
