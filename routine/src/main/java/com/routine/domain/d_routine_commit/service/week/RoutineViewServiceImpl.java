package com.routine.domain.d_routine_commit.service.week;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.c_routine.dto.RoutineViewDTO;
import com.routine.domain.d_routine_commit.model.CommitDraft;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.enums.RoutineCommitStatus;
import com.routine.domain.d_routine_commit.model.TaskCheckStatus;
import com.routine.domain.d_routine_commit.model.week.WeekdayVO;
import com.routine.domain.d_routine_commit.repository.CommitDraftRepository;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineViewServiceImpl implements RoutineViewService {

    private final RoutineRepository routineRepository;
    private final MemberRepository memberRepository;
    private final CommitDraftRepository commitDraftRepository;
    private final WeekService weekService;
    private final CommitLogRepository commitLogRepository;

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
                            .collect(Collectors.toList());

                    draft.setTaskStatuses(statuses);
                    return draft;
                })
                .toList();

        commitDraftRepository.saveAll(toCreate);
    }

    @Override
    public List<RoutineViewDTO> getWeeklyRoutineView(Long memberId, LocalDate today) {
        List<WeekdayVO> week = weekService.getThisWeek(today);
        List<RoutineViewDTO> result = new ArrayList<>();

        for (WeekdayVO weekday : week) {
            switch (weekday.getType()) {
                case PAST -> result.addAll(getPastRoutineView(memberId, weekday.getDate()));
                case TODAY -> result.addAll(getTodayRoutineView(memberId, weekday.getDate()));
                case UPCOMING -> result.addAll(getUpcomingRoutineView(memberId, weekday.getDate()));
            }
        }

        return result;
    }

    private List<RoutineViewDTO> getPastRoutineView(Long memberId, LocalDate date) {
        List<CommitLog> logs = commitLogRepository.findAllByMemberIdAndCommitDate(memberId, date);

        return logs.stream()
                .collect(Collectors.groupingBy(log -> log.getRoutine().getId()))
                .entrySet().stream()
                .map(entry -> RoutineViewDTO.fromCommitLogs(date, entry.getValue()))
                .toList();
    }

    private List<RoutineViewDTO> getTodayRoutineView(Long memberId, LocalDate date) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<CommitDraft> drafts = commitDraftRepository.findAllByMemberAndTargetDate(member, date);

        return drafts.stream()
                .map(RoutineViewDTO::fromDraft)
                .toList();
    }

    private List<RoutineViewDTO> getUpcomingRoutineView(Long memberId, LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        List<Routine> routines = routineRepository.findAllByMemberIdAndRepeatDaysContaining(memberId, dow);

        return routines.stream()
                .map(routine -> RoutineViewDTO.fromRoutine(date, routine))
                .toList();
    }
}
