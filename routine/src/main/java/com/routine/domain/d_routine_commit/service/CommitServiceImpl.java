package com.routine.domain.d_routine_commit.service;



import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final PointService pointService;
    private final CommitRateService commitRateService;


    @Transactional
    @Override
    public void saveTodayCommitLog(Long memberId, CommitRequestDTO dto, LocalDate commitDate) {

        List<CommitLog> logs = commitLogRepository
                .findAllByMemberIdAndRoutineIdAndCommitDate(memberId, dto.getRoutineId(), commitDate);

        if (logs.isEmpty()) {
            throw new IllegalStateException("오늘 날짜의 커밋 로그가 존재하지 않습니다.");
        }

        // 1. 전체 스킵 처리
        if (dto.isSkipped()) {
            logs.forEach(log -> log.setStatus(CommitStatus.SKIP));
            commitRateService.saveSkippedRate(memberId, dto.getRoutineId(), commitDate);
            return;
        }

        // 2. taskStatuses 검증
        List<CommitRequestDTO.TaskStatusDTO> taskStatuses = dto.getTaskStatuses();
        if (taskStatuses == null || taskStatuses.isEmpty()) {
            throw new IllegalArgumentException("태스크 상태 정보가 없습니다.");
        }

        // 3. taskId → status 매핑
        Map<Long, CommitStatus> statusMap = taskStatuses.stream()
                .filter(ts -> ts.getTaskId() != null && ts.getStatus() != null)
                .collect(Collectors.toMap(
                        CommitRequestDTO.TaskStatusDTO::getTaskId,
                        ts -> CommitStatus.valueOf(ts.getStatus()) // enum 매핑
                ));

        // 4. 커밋 로그 갱신
        for (CommitLog log : logs) {
            if (log.getStatus() != CommitStatus.NONE) continue;

            Long taskId = log.getTask().getId();
            CommitStatus newStatus = statusMap.get(taskId);

            if (newStatus != null) {
                log.setStatus(newStatus); // SUCCESS or NONE
            } else {
                log.setStatus(CommitStatus.NONE); // fallback
            }
        }

    }

    @Transactional
    @Override
    public void autoFailUnsubmittedCommits(LocalDate targetDate) {
        List<CommitLog> unsubmitted = commitLogRepository.findAllByCommitDateAndStatus(targetDate, CommitStatus.NONE);
        unsubmitted.forEach(log -> log.setStatus(CommitStatus.FAIL));

    }


}
