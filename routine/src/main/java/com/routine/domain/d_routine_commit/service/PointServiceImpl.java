package com.routine.domain.d_routine_commit.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.model.UserInfo;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.a_member.repository.UserInfoRepository;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import com.routine.domain.d_routine_commit.model.PointLog;
import com.routine.domain.d_routine_commit.model.enums.PointReason;
import com.routine.domain.d_routine_commit.model.enums.PointLogStatus;
import com.routine.domain.d_routine_commit.model.enums.PointFailureReason;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import com.routine.domain.d_routine_commit.repository.PointLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final CommitLogRepository commitLogRepository;
    private final PointLogRepository pointLogRepository;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final UserInfoRepository userInfoRepository;

    private boolean checkPointLimitOrLogFailure(Long memberId, Long routineId) {
        UserInfo userInfo = userInfoRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalStateException("UserInfo 없음"));

        if (userInfo.getTodayAvailablePoint() == 0) {
            pointLogRepository.save(PointLog.builder()
                    .member(userInfo.getMember())
                    .amount(0)
                    .routineId(routineId)
                    .status(PointLogStatus.FAIL)
                    .failureReason(PointFailureReason.DAILY_LIMIT_EXCEEDED)
                    .createdAt(LocalDateTime.now())
                    .build());
            return true; // ✅ 한도 초과로 종료
        }

        return false; // ✅ 진행 가능
    }

    @Override
    @Transactional
    public void rewardPoint(Member member, int amount, PointReason reason, Long routineId) {
        UserInfo userInfo = userInfoRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new IllegalStateException("UserInfo 없음"));

        int available = userInfo.getTodayAvailablePoint();
        int finalAmount = Math.min(amount, available);

        pointLogRepository.save(PointLog.builder()
                .member(member)
                .amount(finalAmount)
                .reason(reason)
                .routineId(routineId)
                .status(PointLogStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build());

        member.addPoint(finalAmount);
        userInfo.setTodayAvailablePoint(available - finalAmount);
    }
    @Override
    @Transactional
    public void rewardForCircleRoutineCommit(Long memberId, Long routineId) {

        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴 없음"));
        // 그룹 루틴 아니라면 포인트 지급 X
        if (!routine.isGroupRoutine()) return;

        // 잔여 포인트 0이라면 종료
        checkPointLimitOrLogFailure(memberId, routineId);

        // 성공률 계산 분모 0 방지
        int total = commitLogRepository.countByMemberIdAndRoutineId(memberId, routineId);
        if (total == 0) return;

        int success = commitLogRepository.countByMemberIdAndRoutineIdAndStatus(
                memberId, routineId, CommitStatus.SUCCESS
        );


        // 포인트 지급 조건 루틴 이행률 70% 이상
        double rate = (double) success / total;
        if (rate < 0.7) {
            pointLogRepository.save(PointLog.builder()
                    .member(memberRepository.findById(memberId).orElse(null))
                    .amount(0)
                    .reason(PointReason.CIRCLE_ROUTINE_COMMIT)
                    .routineId(routineId)
                    .status(PointLogStatus.FAIL)
                    .failureReason(PointFailureReason.LOW_SUCCESS_RATE)
                    .createdAt(LocalDateTime.now())
                    .build());
            return;
        }

        // 중복 지급 예방
        boolean alreadyRewarded = pointLogRepository.existsByMemberIdAndReasonAndDescription(
                memberId,
                PointReason.CIRCLE_ROUTINE_COMMIT,
                "routineId=" + routineId
        );
        if (alreadyRewarded) {
            pointLogRepository.save(PointLog.builder()
                    .member(memberRepository.findById(memberId).orElse(null))
                    .amount(0)
                    .reason(PointReason.CIRCLE_ROUTINE_COMMIT)
                    .routineId(routineId)
                    .status(PointLogStatus.FAIL)
                    .failureReason(PointFailureReason.ALREADY_REWARDED)
                    .createdAt(LocalDateTime.now())
                    .build());
            return;
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버 없음"));

        rewardPoint(
                member,
                5,
                PointReason.CIRCLE_ROUTINE_COMMIT,
                routineId
        );
    }

    @Override
    @Transactional
    public void rewardCircleRoutineCommitPoints() {
        LocalDate today = LocalDate.now();

        // 1. 오늘 커밋된 전체 로그 조회
        List<CommitLog> todayLogs = commitLogRepository.findByCommitDate(today);

        // 2. 중복 제거: (memberId, routineId) 조합 기준으로 추림
        Set<String> processedKeys = new HashSet<>();

        for (CommitLog log : todayLogs) {
            Long memberId = log.getMember().getId();
            Long routineId = log.getRoutine().getId();

            String key = memberId + "-" + routineId;
            if (processedKeys.contains(key)) continue;
            processedKeys.add(key);

            rewardForCircleRoutineCommit(memberId, routineId); // ✅ 개인 리워드 지급
        }
    }


    @Override
    @Transactional
    public void rewardCollectiveBonusForEligibleMembers() {
        LocalDate today = LocalDate.now();

        // 1. 오늘 날짜에 해당하는 커밋 로그 모두 조회
        List<CommitLog> todayLogs = commitLogRepository.findByCommitDate(today);

        // 2. CircleRoutine별 + 멤버별 그룹화
        Map<String, List<CommitLog>> grouped = todayLogs.stream()
                .filter(log -> log.getRoutine().isGroupRoutine()) // ✅ 그룹 루틴 필터링
                .collect(Collectors.groupingBy(log ->
                        log.getRoutine().getId() + ":" + log.getMember().getId()
                ));

        for (Map.Entry<String, List<CommitLog>> entry : grouped.entrySet()) {
            List<CommitLog> logs = entry.getValue();
            if (logs.isEmpty()) continue;

            CommitLog first = logs.get(0);
            Long routineId = first.getRoutine().getId();
            Long memberId = first.getMember().getId();
            Member member = first.getMember();

            // 잔여 포인트 0이라면 종료
            checkPointLimitOrLogFailure(memberId, routineId);

            int total = logs.size();
            int success = (int) logs.stream()
                    .filter(l -> l.getStatus() == CommitStatus.SUCCESS)
                    .count();

            double rate = (double) success / total;
            if (rate < 0.7) {
                // 실패 로그
                pointLogRepository.save(PointLog.builder()
                        .member(member)
                        .amount(0)
                        .reason(PointReason.CIRCLE_ROUTINE_COLLECTIVE_BONUS)
                        .routineId(routineId)
                        .status(PointLogStatus.FAIL)
                        .failureReason(PointFailureReason.LOW_SUCCESS_RATE)
                        .createdAt(LocalDateTime.now())
                        .build());
                continue;
            }

            // 중복 방지
            boolean alreadyRewarded = pointLogRepository.existsByMemberIdAndReasonAndDescription(
                    memberId,
                    PointReason.CIRCLE_ROUTINE_COLLECTIVE_BONUS,
                    "routineId=" + routineId
            );
            if (alreadyRewarded) {
                pointLogRepository.save(PointLog.builder()
                        .member(member)
                        .amount(0)
                        .reason(PointReason.CIRCLE_ROUTINE_COLLECTIVE_BONUS)
                        .routineId(routineId)
                        .status(PointLogStatus.FAIL)
                        .failureReason(PointFailureReason.ALREADY_REWARDED)
                        .createdAt(LocalDateTime.now())
                        .build());
                continue;
            }

            // 성공 보상
            rewardPoint(
                    member,
                    5,
                    PointReason.CIRCLE_ROUTINE_COLLECTIVE_BONUS,
                    routineId
            );
        }
    }

}
