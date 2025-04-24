package com.routine.domain.d_routine_commit.scheduler;


import com.routine.domain.d_routine_commit.service.CommitService;
import com.routine.domain.d_routine_commit.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommitScheduler {

    private final CommitService commitService;
    private final PointService pointService;
    /**
     * 매일 자정에 자동 커밋 실행
     * - 제출하지 않은 Draft 전부 Commit 처리
     * - Draft가 없는 멤버는 해당 날짜 루틴 전부 FAIL 처리
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void autoCommitAndRewardForToday() {
        boolean commitSuccess = commitService.autoCommitForToday(); //  어제 커밋 & 로그 저장

        commitService.initializeTodayDraftsForAll(); //  오늘 루틴 먼저 생성해서 서비스 제공

        if (commitSuccess) {
            pointService.rewardCircleRoutineCommitPoints();           //  개별 포인트 먼저
            pointService.rewardCollectiveBonusForEligibleMembers();   //  그 다음 집단 보상
            commitService.cleanupOldDrafts();                         //  전날 draft 삭제는 맨 마지막
        }
    }


}
