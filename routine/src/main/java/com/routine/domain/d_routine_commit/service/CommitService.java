package com.routine.domain.d_routine_commit.service;

import com.routine.domain.d_routine_commit.dto.CommitDraftForm;
import com.routine.domain.d_routine_commit.model.CommitDraft;

public interface CommitService {


    // 저장하기 위한 Draft 가져오기
    CommitDraft getDraft (Long memberId, Long routineId);

    // 루틴 커밋 임시 저장
    void saveDraft(Long memberId, Long routineId, CommitDraftForm form);

    // 루틴 커밋 제출
    void submitDraft(Long memberId, Long routineId, CommitDraftForm form);

    // 커밋 로그 기록
    void recordCommitLog (CommitDraft draft);


    /**
     * 오늘 날짜 기준, 제출되지 않은 Draft를 자동 커밋하고
     * 커밋 로그 + 포인트 적립까지 처리함.
     * @return 하나라도 커밋 로그가 저장되었는지 여부
     */
    boolean autoCommitForToday();

    /**
     * 오늘 날짜 기준 루틴들에 대해 모든 멤버의 Draft를 초기화함
     * (기존 Draft가 존재하지 않을 경우에만 생성)
     */
    void initializeTodayDraftsForAll();

    /**
     * 지나간 날짜에 대한 draft들 삭제
     * 이미 commitLog로 데이터가 보장되었기 때문에 더미 데이터 삭제
     */
    void cleanupOldDrafts();
}
