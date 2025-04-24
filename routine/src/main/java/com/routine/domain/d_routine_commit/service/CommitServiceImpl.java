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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommitServiceImpl implements CommitService {

    private final CommitLogRepository commitLogRepository;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final PointService pointService;


    @Transactional
    @Override
    public void saveTodayCommitLog(Long memberId, CommitRequestDTO dto) {
        LocalDate today = LocalDate.now();
        List<CommitLog> logs = commitLogRepository
                .findAllByMemberIdAndRoutineIdAndCommitDate(memberId, dto.getRoutineId(), today);

        //  1. 스킵 먼저
        if (dto.isSkipped()) {
            logs.forEach(log -> {
                if (log.getStatus() == CommitStatus.NONE) {
                    log.setStatus(CommitStatus.SKIP);
                }
            });
            return;
        }

        //  2. 체크 리스트 비어 있으면 예외
        if (dto.getCheckedTaskIds().isEmpty()) {
            throw new IllegalArgumentException("체크된 태스크가 없으면 저장할 수 없습니다.");
        }

        //  3. 상태 갱신 (NONE만 변경)
        Set<Long> checkedIds = new HashSet<>(dto.getCheckedTaskIds());
        for (CommitLog log : logs) {
            if (log.getStatus() != CommitStatus.NONE) continue;
            log.setStatus(checkedIds.contains(log.getTask().getId()) ? CommitStatus.SUCCESS : CommitStatus.SKIP);
        }
    }

}
