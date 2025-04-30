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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
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
    public List<CircleSummaryDTO> getMyCircles(Long memberId) {
        List<CircleMember> memberships = circleMemberRepository.findAllByMemberId(memberId);

        return memberships.stream()
                .map(cm -> CircleSummaryDTO.from(cm.getCircle(), cm,
                        circleMemberRepository.countByCircleId(cm.getCircle().getId())))
                .sorted(Comparator
                        .comparing(CircleSummaryDTO::isLeader).reversed()
                        .thenComparing(CircleSummaryDTO::getJoinedAt))
                .collect(Collectors.toList());
    }



    @Override
    public Long createCircle(CircleCreateRequest request, Long leaderId) {

        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("리더를 찾을 수 없습니다."));

        Circle circle = Circle.builder()
                .name(request.getCircleName())
                .description(request.getCircleDescription())
                .tags(request.getTags())
                .category(Category.valueOf(request.getCategory()))
                .detailCategory(DetailCategory.valueOf(request.getDetailCategory()))
                .isOpened(request.isOpened())
                .maxMemberCount(request.getMaxMemberCount())
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
    public CircleResponse getCircleDetail(Long circleId, Long memberId) {
        LocalDate today = LocalDate.now();

        // 1. 공용 루틴 조회
        CircleRoutineDTO circleRoutine = findCircleRoutine(circleId);

        // 2. 오늘 멤버 커밋 이력 조회
        CircleRoutineCommits commitsToday = getCommitsByCircleId(circleId, today);

        // 3. 로그인된 서클 회원 정보
        AuthorizationDTO authorizationDTO = checkAuthorization(circleId, memberId);

        // 4. 서클 멤버 리스트 조회
        List<CircleMemberDTO> memberDTOs = getMemberDTOsByCircleId(circleId);

        // 5. DTO 조립
        return new CircleResponse(circleRoutine, commitsToday, authorizationDTO, memberDTOs);
    }

    @Override
    public List<RoutineSummaryDTO> getMyRoutinesForCircle(Long memberId, String detailCategory) {
        DetailCategory detailCategoryEnum = DetailCategory.valueOf(detailCategory);
        List<Routine> routines = routineRepository.findPersonalRoutinesByDetailCategory(memberId, detailCategoryEnum);

        return routines.stream()
                .map(r -> new RoutineSummaryDTO(
                        r.getId(),
                        r.getTitle(),
                        r.getCategory().name(),
                        r.getDetailCategory().name()
                ))
                .collect(Collectors.toList());
    }


    private AuthorizationDTO checkAuthorization(Long circleId, Long memberId) {

        boolean leader = circleMemberRepository.existsByCircleIdAndMemberIdAndRole(circleId, memberId, CircleMember.Role.LEADER);
        boolean member = circleMemberRepository.existsByCircleIdAndMemberId(circleId, memberId);
        String nickname = memberRepository.findNicknameById(memberId);
        return new AuthorizationDTO(leader, member, memberId, nickname);
    }

    private CircleRoutineDTO findCircleRoutine(Long circleId) {
        Long adminId = circleMemberRepository.findAdminIdByCircleId(circleId)
                .orElseThrow(() -> new IllegalArgumentException("리더가 존재하지 않는 서클입니다."));

        Routine routine = routineRepository.findByCircleIdAndMemberId(circleId, adminId)
                .orElseThrow(() -> new IllegalArgumentException("해당 서클에 루틴이 존재하지 않습니다."));

        List<RoutineTask> tasks = routineTaskRepository.findAllByRoutineIdOrderByOrderNumberAsc(routine.getId());

        return CircleRoutineDTO.from(routine, tasks);
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

    @Override
    public List<CircleSummaryDTO> searchCircles(String category, String detailCategory, String keyword) {
        Category categoryEnum = null;

        if (category != null && !category.isEmpty()) {
            try {
                categoryEnum = Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("잘못된 category 값: " + category);
            }
        }

        DetailCategory detailCategoryEnum = null;
        if (detailCategory != null && !detailCategory.isEmpty()) {
            try {
                detailCategoryEnum = DetailCategory.valueOf(detailCategory);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("잘못된 detailCategory 값: " + detailCategory);
            }
        }

        List<Circle> result = circleRepository.searchCircles(categoryEnum, detailCategoryEnum, keyword);

        return result.stream()
                .map(circle -> {
                    int currentCount = circleMemberRepository.countByCircleId(circle.getId());
                    return CircleSummaryDTO.from(circle, currentCount);
                })
                .collect(Collectors.toList());
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

    public List<CircleMemberDTO> getMemberDTOsByCircleId(Long circleId) {
        return circleMemberRepository.findAllByCircleId(circleId).stream()
                .map(cm -> new CircleMemberDTO(
                        cm.getMember().getId(),
                        cm.getMember().getNickname()
                ))
                .collect(Collectors.toList());
    }

}
