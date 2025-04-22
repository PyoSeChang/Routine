package com.routine.domain.d_routine_commit.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.d_routine_commit.dto.CommitDraftForm;
import com.routine.domain.d_routine_commit.dto.RoutineDraftDTO;
import com.routine.domain.d_routine_commit.model.CommitDraft;

import java.util.List;

public interface CommitService {
    // 최초 로그인 시 루틴 초안 만들기
    void initializeTodayDrafts(Long memberId);

    // 오늘할 루틴들의 초안 가져오기
    List<RoutineDraftDTO> getTodayDrafts(Long memberId);

    // 저장하기 위한 Draft 가져오기
    CommitDraft getDraft (Long memberId, Long routineId);

    // 루틴 커밋 임시 저장
    void saveDraft(Long memberId, Long routineId, CommitDraftForm form);

    // 루틴 커밋 제출
    void submitDraft(Long memberId, Long routineId, CommitDraftForm form);
}
