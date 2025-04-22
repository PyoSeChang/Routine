package com.routine.domain.d_routine_commit.service;



import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.dto.CommitDraftForm;
import com.routine.domain.d_routine_commit.dto.RoutineDraftDTO;
import com.routine.domain.d_routine_commit.model.CommitDraft;
import com.routine.domain.d_routine_commit.model.CommitStatus;
import com.routine.domain.d_routine_commit.model.RoutineCommitStatus;
import com.routine.domain.d_routine_commit.model.TaskCheckStatus;
import com.routine.domain.d_routine_commit.repository.CommitDraftRepository;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommitServiceImpl implements CommitService {

    private final CommitLogRepository commitRepository;
    private final MemberRepository memberRepository;
    private final CommitDraftRepository commitDraftRepository;
    private final RoutineRepository routineRepository;

    @Override
    public void initializeTodayDrafts(Long memberId) {
        LocalDate today = LocalDate.now();
        DayOfWeek dow = today.getDayOfWeek();

        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다: ID=" + memberId)
        );

        List<Routine> todayRoutines = routineRepository.findAllByMemberAndRepeatDaysContaining(member, dow);
        List<CommitDraft> existingDrafts = commitDraftRepository.findAllByMemberAndTargetDate(member, today);

        Set<Long> draftedIds = existingDrafts.stream()
                .map(d -> d.getRoutine().getId())
                .collect(Collectors.toSet());

        List<CommitDraft> toCreate = todayRoutines.stream()
                .filter(r -> !draftedIds.contains(r.getId()))
                .map(r -> {
                    CommitDraft draft = CommitDraft.builder()
                            .routine(r)
                            .member(member)
                            .targetDate(today)
                            .status(RoutineCommitStatus.DRAFT)
                            .build();

                    // ✅ 루틴의 태스크 목록 기반으로 TaskCheckStatus 생성
                    List<TaskCheckStatus> statuses = r.getRoutineTasks().stream()
                            .map(task -> TaskCheckStatus.builder()
                                    .taskId(task.getId())
                                    .content(task.getContent())
                                    .checked(false)
                                    .commitDraft(draft) // 양방향 연결
                                    .build())
                            .toList();

                    draft.setTaskStatuses(statuses);
                    return draft;
                })
                .toList();

        commitDraftRepository.saveAll(toCreate);
    }


    @Override
    @Transactional(readOnly = true)
    public List<RoutineDraftDTO> getTodayDrafts(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        LocalDate today = LocalDate.now();
        List<CommitDraft> drafts = commitDraftRepository.findAllByMemberAndTargetDate(member, today);

        return drafts.stream()
                .map(RoutineDraftDTO::fromDraft)
                .toList();
    }

    @Override
    public CommitDraft getDraft(Long memberId, Long routineId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 루틴입니다."));
        LocalDate today = LocalDate.now();

        return commitDraftRepository.findByMemberAndRoutineAndTargetDate(member, routine, today)
                .orElseThrow(() -> new IllegalStateException("오늘 날짜의 드래프트가 존재하지 않습니다."));
    }


    @Override
    public void saveDraft(Long memberId, Long routineId, CommitDraftForm form) {
        CommitDraft draft = getDraft(memberId, routineId);

        // 기존 상태 전부 제거 (orphanRemoval = true)
        draft.getTaskStatuses().clear();

        // 새 상태들 할당
        List<TaskCheckStatus> statuses = form.getCheckedTaskIds().stream()
                .map(taskId -> TaskCheckStatus.builder()
                        .taskId(taskId)
                        .commitDraft(draft)
                        .build())
                .toList();

        draft.setTaskStatuses(statuses);
        draft.setStatus(RoutineCommitStatus.DRAFT);

        commitDraftRepository.save(draft);
    }


    @Override
    public void submitDraft(Long memberId, Long routineId, CommitDraftForm form) {
        CommitDraft draft = getDraft(memberId, routineId);

        // 기존 체크 상태 초기화
        draft.getTaskStatuses().clear();

        // 새로운 체크 상태 등록
        List<TaskCheckStatus> statuses = form.getCheckedTaskIds().stream()
                .map(taskId -> TaskCheckStatus.builder()
                        .taskId(taskId)
                        .commitDraft(draft)
                        .build())
                .toList();

        draft.setTaskStatuses(statuses);
        draft.setStatus(RoutineCommitStatus.SUBMITTED);

        commitDraftRepository.save(draft);
    }


}
