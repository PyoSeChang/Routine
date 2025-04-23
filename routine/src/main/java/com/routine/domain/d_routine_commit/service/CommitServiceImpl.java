package com.routine.domain.d_routine_commit.service;



import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.dto.CommitDraftForm;
import com.routine.domain.d_routine_commit.dto.RoutineDraftDTO;
import com.routine.domain.d_routine_commit.model.*;
import com.routine.domain.d_routine_commit.repository.CommitDraftRepository;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommitServiceImpl implements CommitService {

    private final CommitLogRepository commitLogRepository;
    private final MemberRepository memberRepository;
    private final CommitDraftRepository commitDraftRepository;
    private final RoutineRepository routineRepository;



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
    @Transactional
    public void saveDraft(Long memberId, Long routineId, CommitDraftForm form) {
        CommitDraft draft = getDraft(memberId, routineId);

        if (draft.getStatus() == RoutineCommitStatus.SUBMITTED) {
            throw new IllegalStateException("이미 제출된 커밋은 수정할 수 없습니다.");
        }


        // 기존 상태 전부 제거 (orphanRemoval = true)
        draft.getTaskStatuses().clear();

        // 새 상태들 추가 (기존 리스트에 덧붙임)
        List<TaskCheckStatus> statuses = form.getCheckedTaskIds().stream()
                .map(taskId -> TaskCheckStatus.builder()
                        .taskId(taskId)
                        .checked(true)
                        .commitDraft(draft)
                        .build())
                .collect(Collectors.toList());

        draft.getTaskStatuses().addAll(statuses); // addAll로 기존 리스트 유지

        draft.setStatus(RoutineCommitStatus.DRAFT);
        commitDraftRepository.save(draft);
    }


    @Override
    @Transactional
    public void submitDraft(Long memberId, Long routineId, CommitDraftForm form) {
        CommitDraft draft = getDraft(memberId, routineId);
        System.out.println("바운딩 테스트: "+form.isSkipped());
        System.out.println("바운딩 테스트: "+form.isSkipped());
        System.out.println("바운딩 테스트: "+form.isSkipped());
        System.out.println("바운딩 테스트: "+form.isSkipped());
        draft.setSkipped(form.isSkipped());
        draft.getTaskStatuses().clear();

        if (draft.getStatus() == RoutineCommitStatus.SUBMITTED) {
            throw new IllegalStateException("이미 제출된 루틴입니다.");
        }

        if (!form.isSkipped()) {
            // ✅ 스킵 안 한 경우에만 task 상태 생성
            List<TaskCheckStatus> statuses = Optional.ofNullable(form.getCheckedTaskIds())
                    .orElse(List.of()) // null 방어
                    .stream()
                    .map(taskId -> TaskCheckStatus.builder()
                            .taskId(taskId)
                            .checked(true)
                            .commitDraft(draft)
                            .build())
                    .collect(Collectors.toList());

            draft.getTaskStatuses().addAll(statuses);
        }

        draft.setStatus(RoutineCommitStatus.SUBMITTED);
        commitDraftRepository.save(draft);

        recordCommitLog(draft);
    }

    public void recordCommitLog(CommitDraft draft) {
        Member member = draft.getMember();
        Routine routine = draft.getRoutine();
        LocalDate commitDate = LocalDate.now();

        boolean skipped = draft.isSkipped();

        Set<Long> checkedIds = draft.getTaskStatuses().stream()
                .filter(TaskCheckStatus::isChecked)
                .map(TaskCheckStatus::getTaskId)
                .collect(Collectors.toSet());

        List<CommitLog> logs = routine.getRoutineTasks().stream()
                .map(task -> {
                    CommitStatus status;

                    if (skipped) {
                        status = CommitStatus.SKIP; // 스킵된 커밋은 모든 태스크 SKIP
                    } else {
                        status = checkedIds.contains(task.getId()) ?
                                CommitStatus.SUCCESS :
                                CommitStatus.FAIL;
                    }

                    return CommitLog.builder()
                            .routine(routine)
                            .member(member)
                            .commitDate(commitDate)
                            .taskId(task.getId())
                            .status(status)
                            .build();
                })
                .toList();

        commitLogRepository.saveAll(logs);
    }


}
