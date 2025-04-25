package com.routine.domain.d_routine_commit.service;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.CommitRate;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import com.routine.domain.d_routine_commit.repository.CommitRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommitRateServiceImpl implements CommitRateService {

    private final CommitRateRepository commitRateRepository;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final CommitLogRepository commitLogRepository;



    @Transactional
    @Override
    public double updateRecentRate(Long memberId, Long routineId, LocalDate commitDate) {


        // 1. 오늘 날짜 커밋 로그 가져오기
        List<CommitLog> logs = commitLogRepository
                .findAllByMemberIdAndRoutineIdAndCommitDate(memberId, routineId, commitDate);

        if (logs.isEmpty()) return 0.0;

        // 2. 성공률 계산
        long total = logs.size();
        long success = logs.stream()
                .filter(log -> log.getStatus() == CommitStatus.SUCCESS)
                .count();
        double rate = (double) success / total;

        // 3. 존재 여부 체크 (commitDate 기준)
        Optional<CommitRate> optional = commitRateRepository
                .findByMemberIdAndRoutineIdAndCommitDate(memberId, routineId, commitDate);

        CommitRate rateRecord;
        if (optional.isPresent()) {
            rateRecord = optional.get();
            rateRecord.setCommitRate(rate);
        } else {
            rateRecord = CommitRate.builder()
                    .member(memberRepository.findById(memberId).orElseThrow())
                    .routine(routineRepository.findById(routineId).orElseThrow())
                    .commitDate(commitDate)
                    .commitRate(rate)
                    .year(null)
                    .week(null)
                    .build();
        }

        commitRateRepository.save(rateRecord);
        return rate;
    }

    @Override
    public void saveSkippedRate(Long memberId, Long routineId, LocalDate commitDate) {
        CommitRate rate = CommitRate.builder()
                .routine(routineRepository.findById(routineId).orElseThrow())
                .member(memberRepository.findById(memberId).orElseThrow())
                .commitDate(commitDate)
                .commitRate(0.0)
                .skipped(true) // ✅ 핵심
                .build();

        commitRateRepository.save(rate);
    }

    @Override
    @Transactional
    public void updateRateByAutoCommit(LocalDate targetDate) {
        List<Long> routineIds = commitLogRepository.findRoutineIdsByCommitDate(targetDate);

        for (Long routineId : routineIds) {
            // 🔥 SKIP 아닌 로그만 필터링
            List<CommitLog> logs = commitLogRepository
                    .findAllByRoutineIdAndCommitDateAndStatusNot(routineId, targetDate, CommitStatus.SKIP);

            if (logs.isEmpty()) continue;

            long total = logs.size();
            long success = logs.stream()
                    .filter(log -> log.getStatus() == CommitStatus.SUCCESS)
                    .count();

            double rate = (double) success / total;

            commitRateRepository.save(
                    CommitRate.builder()
                            .routine(routineRepository.findById(routineId).orElseThrow())
                            .member(logs.get(0).getMember()) // 어차피 루틴별 개별 커밋
                            .commitDate(targetDate)
                            .commitRate(rate)
                            .skipped(false)
                            .build()
            );
        }
    }



}
