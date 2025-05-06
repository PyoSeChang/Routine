package com.routine.domain.d_routine_commit.service;



import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.b_circle.service.CircleMemberService;
import com.routine.domain.c_routine.dto.CommitLogDTO;
import com.routine.domain.c_routine.dto.TaskDTO;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.dto.CommitRequestDTO;
import com.routine.domain.d_routine_commit.model.*;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import com.routine.domain.d_routine_commit.model.enums.RoutineCommitStatus;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommitServiceImpl implements CommitService {

    private final CommitLogRepository commitLogRepository;
    private final RoutineRepository routineRepository;
    private final CommitRateService commitRateService;
    private final CommitMessageService commitMessageService;
    private final CircleMemberService circleMemberService;

    @Transactional
    @Override
    public void saveTodayCommitLog(Long memberId, CommitRequestDTO dto, LocalDate commitDate) {
        List<CommitLog> logs = commitLogRepository
                .findAllByMemberIdAndRoutineIdAndCommitDate(memberId, dto.getRoutineId(), commitDate);

        if (logs.isEmpty()) {
            throw new IllegalStateException("오늘 날짜의 커밋 로그가 존재하지 않습니다.");
        }

        if (dto.isSkipped()) {
            handleSkipCommit(memberId, dto.getRoutineId(), commitDate, logs);
            commitMessageService.saveCommitMessage(memberId, dto.getRoutineId(), commitDate, dto.getMessage(), dto.getIsPublic());
        } else {
            handleTaskCommit(dto, logs);
            commitMessageService.saveCommitMessage(memberId, dto.getRoutineId(), commitDate, dto.getMessage(), dto.getIsPublic());
        }
    }

    private void handleSkipCommit(Long memberId, Long routineId, LocalDate commitDate, List<CommitLog> logs) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

        if (routine.isGroupRoutine()) {
            int skipCount = circleMemberService.getSkipCount(memberId, routineId);
            if (skipCount <= 0) {
                throw new IllegalStateException("스킵 기회를 모두 소진했습니다.");
            }
            circleMemberService.decrementSkipCount(memberId, routineId);
        }

        logs.forEach(log -> log.setStatus(CommitStatus.SKIP));
        commitRateService.saveSkippedRate(memberId, routineId, commitDate);
    }

    private void handleTaskCommit(CommitRequestDTO dto, List<CommitLog> logs) {
        List<CommitRequestDTO.TaskStatusDTO> taskStatuses = dto.getTaskStatuses();

        if (taskStatuses == null || taskStatuses.isEmpty()) {
            throw new IllegalArgumentException("태스크 상태 정보가 없습니다.");
        }

        Map<Long, CommitStatus> statusMap = taskStatuses.stream()
                .filter(ts -> ts.getTaskId() != null && ts.getStatus() != null)
                .collect(Collectors.toMap(
                        CommitRequestDTO.TaskStatusDTO::getTaskId,
                        ts -> CommitStatus.valueOf(ts.getStatus())
                ));

        for (CommitLog log : logs) {
            Long taskId = log.getTask().getId();
            CommitStatus newStatus = statusMap.get(taskId);

            if (log.getStatus() == CommitStatus.NONE && newStatus == CommitStatus.SUCCESS) {
                log.setStatus(CommitStatus.SUCCESS);
            }
        }
    }



    @Override
    public List<LocalDate> getCommitDatesByRoutineId(Long memberId, Long routineId) {
        return commitLogRepository.findCommitDates(memberId, routineId);
    }

    @Override
    public CommitLogDTO getCommitLogSummary(Long memberId, Long routineId, LocalDate date) {
        List<CommitLog> logs = commitLogRepository.findAllByMemberIdAndRoutineIdAndCommitDate(memberId, routineId, date);

//        if (logs.isEmpty()) {
//            throw new CommitNotFoundException();
//        }

        String routineName = logs.get(0).getRoutine().getTitle();

        List<TaskDTO> taskDTOs = logs.stream()
                .map(log -> TaskDTO.builder()
                        .taskId(log.getTask().getId())
                        .content(log.getTask().getContent())
                        .status(log.getStatus().name())
                        .checked(log.getStatus() == CommitStatus.SUCCESS)
                        .build()
                )
                .toList();

        return new CommitLogDTO(routineName, taskDTOs);
    }



}
