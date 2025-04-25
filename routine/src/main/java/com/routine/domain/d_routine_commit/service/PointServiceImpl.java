package com.routine.domain.d_routine_commit.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.model.UserInfo;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.a_member.repository.UserInfoRepository;
import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.b_circle.model.CircleMember;
import com.routine.domain.b_circle.repository.CircleMemberRepository;
import com.routine.domain.b_circle.repository.CircleRepository;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.CommitRate;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import com.routine.domain.d_routine_commit.model.PointLog;
import com.routine.domain.d_routine_commit.model.enums.PointReason;
import com.routine.domain.d_routine_commit.model.enums.PointLogStatus;
import com.routine.domain.d_routine_commit.model.enums.PointFailureReason;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import com.routine.domain.d_routine_commit.repository.CommitRateRepository;
import com.routine.domain.d_routine_commit.repository.PointLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final CommitLogRepository commitLogRepository;
    private final PointLogRepository pointLogRepository;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final UserInfoRepository userInfoRepository;
    private final CommitRateRepository commitRateRepository;
    private final CircleRepository circleRepository;
    private final CircleMemberRepository circleMemberRepository;

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
    public void rewardPoint(Member member, int amount, PointReason reason, Long routineId, LocalDate commitDate) {
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
                .commitDate(commitDate) // ✅ 커밋 기준일 추가!
                .createdAt(LocalDateTime.now())
                .build());

        member.addPoint(finalAmount);
        userInfo.setTodayAvailablePoint(available - finalAmount);
    }


    @Override
    @Transactional
    public void rewardForCircleRoutineCommit(Long memberId, Long routineId, LocalDate commitDate) {
        // 1. 포인트 한도 체크 (내부에서 실패 로그 남김)
        checkPointLimitOrLogFailure(memberId, routineId);

        // 2. 중복 지급 방지 - commitDate 기준!
        boolean alreadyRewarded = pointLogRepository.existsByMemberIdAndReasonAndRoutineIdAndCommitDate(
                memberId,
                PointReason.CIRCLE_ROUTINE_COMMIT,
                routineId,
                commitDate
        );
        if (alreadyRewarded) {
            pointLogRepository.save(PointLog.builder()
                    .member(memberRepository.findById(memberId).orElse(null))
                    .amount(0)
                    .reason(PointReason.CIRCLE_ROUTINE_COMMIT)
                    .routineId(routineId)
                    .status(PointLogStatus.FAIL)
                    .failureReason(PointFailureReason.ALREADY_REWARDED)
                    .commitDate(commitDate)
                    .createdAt(LocalDateTime.now())
                    .build());
            return;
        }

        // 3. 포인트 지급
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버 없음"));

        rewardPoint(
                member,
                5,
                PointReason.CIRCLE_ROUTINE_COMMIT,
                routineId,
                commitDate
        );
    }


    @Transactional
    @Override
    public void rewardCircleRoutineCommitToAll(LocalDate targetDate) {

        // 1. 어제 커밋 로그 전부 조회 (NONE은 오토커밋 이후 없다고 가정)
        List<CommitLog> logs = commitLogRepository.findAllByCommitDate(targetDate);

        // 2. 그룹 루틴 필터 + 멤버-루틴 조합으로 묶기
        Map<String, List<CommitLog>> grouped = logs.stream()
                .filter(log -> log.getRoutine().isGroupRoutine())
                .collect(Collectors.groupingBy(log ->
                        log.getMember().getId() + ":" + log.getRoutine().getId()));

        for (Map.Entry<String, List<CommitLog>> entry : grouped.entrySet()) {
            String[] parts = entry.getKey().split(":");
            Long memberId = Long.valueOf(parts[0]);
            Long routineId = Long.valueOf(parts[1]);

            // 3. 어제자 포인트 지급 여부 확인
            boolean alreadyRewarded = pointLogRepository.existsByMemberIdAndReasonAndRoutineIdAndCreatedAtBetween(
                    memberId,
                    PointReason.CIRCLE_ROUTINE_COMMIT,
                    routineId,
                    targetDate.atStartOfDay(),
                    targetDate.plusDays(1).atStartOfDay()
            );
            if (alreadyRewarded) continue;


            Optional<CommitRate> rateOpt = commitRateRepository.findByMemberIdAndRoutineIdAndCommitDate(memberId, routineId, targetDate);
            if (rateOpt.isEmpty()) continue;
            double rate = rateOpt.get().getCommitRate();

            Member member = memberRepository.findById(memberId).orElse(null);
            if (member == null) continue;

            // 4. 조건 충족 여부에 따라 로그 작성 및 지급
            if (rate >= 0.7) {
                rewardPoint(member, 5, PointReason.CIRCLE_ROUTINE_COMMIT, routineId, targetDate);
            } else {
                pointLogRepository.save(PointLog.builder()
                        .member(member)
                        .amount(0)
                        .reason(PointReason.CIRCLE_ROUTINE_COMMIT)
                        .routineId(routineId)
                        .status(PointLogStatus.FAIL)
                        .failureReason(PointFailureReason.LOW_SUCCESS_RATE)
                        .commitDate(targetDate)
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        }
    }


    @Transactional
    @Override
    public void rewardCollectiveBonusForEligibleMembers(LocalDate targetDate) {

        // 1. 어제 CIRCLE_ROUTINE_COMMIT 보상 받은 사람들만 조회
        List<PointLog> baseLogs = pointLogRepository.findAllByReasonAndCommitDate(
                PointReason.CIRCLE_ROUTINE_COMMIT,
                targetDate
        );

        // 2. 참여한 Circle ID들만 중복 없이 수집
        Set<Long> participatedCircleIds = baseLogs.stream()
                .map(log -> {
                    Routine routine = routineRepository.findById(log.getRoutineId()).orElse(null);
                    return (routine != null && routine.getCircle() != null) ? routine.getCircle().getId() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 3. Circle별 참여율 계산 후, 70% 이상인 Circle만 추림
        Set<Long> eligibleCircleIds = new HashSet<>();
        for (Long circleId : participatedCircleIds) {
            Circle circle = circleRepository.findById(circleId).orElse(null);
            if (circle == null) continue;

            List<CircleMember> members = circleMemberRepository.findAllByCircleId(circleId);

            long total = 0;
            long success = 0;
            for (CircleMember cm : members) {
                Optional<CommitLog> logOpt = commitLogRepository.findByMemberIdAndRoutineCircleIdAndCommitDate(cm.getMember().getId(), circleId, targetDate);
                if (logOpt.isPresent()) {
                    CommitLog log = logOpt.get();
                    if (log.getStatus() != CommitStatus.SKIP) {
                        total++;
                        if (log.getStatus() == CommitStatus.SUCCESS) success++;
                    }
                }
            }

            if (total > 0 && (double) success / total >= 0.7) {
                eligibleCircleIds.add(circleId);
            }
        }

        // 4. 보상 지급: eligibleCircleIds에 속한 멤버 중 baseLogs에 있는 멤버만
        for (PointLog log : baseLogs) {
            Routine routine = routineRepository.findById(log.getRoutineId()).orElse(null);
            if (routine == null || routine.getCircle() == null) continue;

            Long circleId = routine.getCircle().getId();
            if (!eligibleCircleIds.contains(circleId)) continue;

            Member member = log.getMember();
            if (pointLogRepository.existsByMemberIdAndReasonAndRoutineIdAndCreatedAtBetween(
                    member.getId(),
                    PointReason.CIRCLE_ROUTINE_COLLECTIVE_BONUS,
                    routine.getId(),
                    targetDate.atStartOfDay(),
                    targetDate.plusDays(1).atStartOfDay()
            )) continue;

            pointLogRepository.save(PointLog.builder()
                    .member(member)
                    .routineId(routine.getId())
                    .amount(5)
                    .reason(PointReason.CIRCLE_ROUTINE_COLLECTIVE_BONUS)
                    .status(PointLogStatus.SUCCESS)
                    .commitDate(targetDate)
                    .createdAt(LocalDateTime.now())
                    .build());
        }
    }


}
