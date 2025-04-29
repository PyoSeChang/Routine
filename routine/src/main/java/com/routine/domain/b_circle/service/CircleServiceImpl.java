package com.routine.domain.b_circle.service;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.b_circle.dto.*;
import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.b_circle.model.CircleMember;
import com.routine.domain.b_circle.repository.CircleMemberRepository;
import com.routine.domain.b_circle.repository.CircleRepository;
import com.routine.domain.b_circle.dto.CircleRoutineCommits;
import com.routine.domain.c_routine.dto.TaskDTO;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.model.RoutineTask;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.c_routine.repository.RoutineTaskRepository;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.CommitMessage;
import com.routine.domain.d_routine_commit.model.CommitRate;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import com.routine.domain.d_routine_commit.repository.CommitMessageRepository;
import com.routine.domain.d_routine_commit.repository.CommitRateRepository;
import com.routine.domain.e_board.model.Category;
import com.routine.domain.e_board.model.DetailCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CircleServiceImpl implements CircleService {

    private final CircleRepository circleRepository;
    private final MemberRepository memberRepository;
    private final CircleMemberRepository circleMemberRepository;
    private final RoutineRepository routineRepository;
    private final CommitLogRepository commitLogRepository;
    private final CommitRateRepository commitRateRepository;
    private final CommitMessageRepository commitMessageRepository;
    private final RoutineTaskRepository routineTaskRepository;

    @Override
    public List<CircleSummary> getMyCircles(Long memberId) {
        return List.of();
    }

    @Override
    public Long createCircle(CircleCreateRequest request) {
        Long leaderId = request.getLeaderId();
        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("리더를 찾을 수 없습니다."));

        Circle circle = Circle.builder()
                .name(request.getName())
                .description(request.getDescription())
                .tags(request.getTags())
                .isPublic(request.isOpened())
                .category(Category.valueOf(request.getCategory()))
                .detailCategory(DetailCategory.valueOf(request.getDetailCategory()))
                .build();

        circleRepository.save(circle);

        // CircleMember 엔티티도 생성
        CircleMember circleMember = CircleMember.builder()
                .circle(circle)
                .member(leader)
                .role(CircleMember.Role.LEADER)
                .skipCount(0)
                .build();

        circleMemberRepository.save(circleMember);

        return circle.getId();
    }


    @Override
    public CircleResponse getCircleDetail(Long circleId) {
        LocalDate today = LocalDate.now();

        // 1. 공용 루틴 조회
        CircleRoutineDTO circleRoutine = findCircleRoutine(circleId);

        // 2. 오늘 멤버 커밋 이력 조회
        CircleRoutineCommits commitsToday = getCommitsByCircleId(circleId, today);

        // 3. 서클 회원 목록
        List<Long> circleMembers = circleMemberRepository.findMemberIdsByCircleId(circleId);

        // 3. DTO 조립
        return new CircleResponse(circleRoutine, commitsToday, circleMembers);

    }

    private CircleRoutineDTO findCircleRoutine(Long circleId) {
        Routine routine = routineRepository.findTopByCircleId(circleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 서클에 루틴이 존재하지 않습니다."));

        List<RoutineTask> tasks = routineTaskRepository.findAllByRoutineIdOrderByOrderNumberAsc(routine.getId());

        return CircleRoutineDTO.from(routine, tasks);
    }

    @Override
    public List<RoutineSummaryDTO> getMyRoutinesForCircle(Long memberId, Long circleId) {
        Circle circle = circleRepository.findById(circleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서클입니다"));

        // circle의 detailCategory 가져오기
        var circleDetailCategory = circle.getDetailCategory();

        // 내 개인 루틴 중, 카테고리가 일치하는 루틴만 가져오기
        List<Routine> routines = routineRepository.findPersonalRoutinesByDetailCategory(memberId, circleDetailCategory);

        return routines.stream()
                .map(r -> new RoutineSummaryDTO(r.getId(), r.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public void convertRoutineToCircle(Long routineId, Long circleId, Long memberId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 루틴입니다"));

        if (!routine.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인 소유의 루틴만 변환할 수 있습니다");
        }

        Circle circle = circleRepository.findById(circleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서클입니다"));

        // 루틴을 서클 루틴으로 전환
        routine.setGroupRoutine(true);
        routine.setCircle(circle);

        routineRepository.save(routine);
    }

    @Override
    public CircleRoutineCommits getCommitsByCircleId(Long circleId, LocalDate commitDate) {
        List<Long> memberIds = circleMemberRepository.findMemberIdsByCircleId(circleId);
        if (memberIds.isEmpty()) {
            return new CircleRoutineCommits(List.of());
        }

        // 🆕 멤버들 조회
        List<Member> members = memberRepository.findAllById(memberIds);
        Map<Long, String> memberIdToNickname = members.stream()
                .collect(Collectors.toMap(Member::getId, Member::getNickname));

        List<CommitLog> commitLogs = commitLogRepository
                .findAllByMemberIdInAndCommitDateAndRoutine_Circle_Id(memberIds, commitDate, circleId);
        List<CommitMessage> commitMessages = commitMessageRepository.findAllByMemberIdInAndCommitDate(memberIds, commitDate);
        List<CommitRate> commitRates = commitRateRepository.findAllByMemberIdInAndCommitDate(memberIds, commitDate);

        Map<Long, List<TaskDTO>> tasksByMemberId = mapTasksByMember(commitLogs);
        Map<Long, String> messageByMemberId = mapMessagesByMember(commitMessages);
        Map<Long, Double> rateByMemberId = mapRatesByMember(commitRates);

        List<CircleRoutineCommits.MemberCommitInfo> memberCommitInfos = memberIds.stream()
                .map(memberId -> buildMemberCommitInfo(
                        memberId,
                        memberIdToNickname.getOrDefault(memberId, "Unknown"),
                        rateByMemberId.getOrDefault(memberId, 0.0),
                        tasksByMemberId.getOrDefault(memberId, List.of()),
                        messageByMemberId.get(memberId)
                ))
                .collect(Collectors.toList());

        return new CircleRoutineCommits(memberCommitInfos);
    }


    private Map<Long, List<TaskDTO>> mapTasksByMember(List<CommitLog> commitLogs) {
        return commitLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getMember().getId(),
                        Collectors.mapping(TaskDTO::from, Collectors.toList())
                ));
    }

    private Map<Long, String> mapMessagesByMember(List<CommitMessage> commitMessages) {
        return commitMessages.stream()
                .collect(Collectors.toMap(
                        msg -> msg.getMember().getId(),
                        CommitMessage::getMessage
                ));
    }

    private Map<Long, Double> mapRatesByMember(List<CommitRate> commitRates) {
        return commitRates.stream()
                .collect(Collectors.toMap(
                        rate -> rate.getMember().getId(),
                        CommitRate::getCommitRate
                ));
    }

    private CircleRoutineCommits.MemberCommitInfo buildMemberCommitInfo(
            Long memberId,
            String nickname,
            Double commitRate,
            List<TaskDTO> tasks,
            String commitMessage
    ) {
        return CircleRoutineCommits.MemberCommitInfo.builder()
                .memberId(memberId)
                .nickname(nickname)
                .commitRate(commitRate)
                .tasks(tasks)
                .commitMessage(commitMessage)
                .build();
    }

}
