package com.routine.domain.b_circle.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.b_circle.model.CircleMember;
import com.routine.domain.b_circle.repository.CircleMemberRepository;
import com.routine.domain.b_circle.repository.CircleRepository;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.c_routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CircleMemberServiceImpl implements CircleMemberService {

    private final CircleMemberRepository circleMemberRepository;
    private final RoutineRepository routineRepository;
    private final CircleRepository circleRepository;
    private final RoutineService routineService;

    @Override
    @Transactional(readOnly = true)
    public int getSkipCount(Long memberId, Long routineId) {
        Long circleId = routineRepository.findCircleIdById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴에 연결된 서클이 없습니다."));

//        return circleMemberRepository.findByMemberIdAndCircleId(memberId, circleId)
//                .map(CircleMember::getSkipCount)
//                .orElse(0);
        int skipCounts=3;
        return skipCounts;

    }

    @Override
    @Transactional
    public void decrementSkipCount(Long memberId, Long routineId) {
        Long circleId = routineRepository.findCircleIdById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴에 연결된 서클이 없습니다."));

        CircleMember circleMember = circleMemberRepository.findByMemberIdAndCircleId(memberId, circleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버의 서클 루틴 기록을 찾을 수 없습니다."));

        int currentCount = circleMember.getSkipCount();
        circleMember.setSkipCount(Math.max(currentCount - 1, 0));
    }


    @Override
    public void resetAllSkipCounts() {
        circleMemberRepository.updateSkipCountToThreeForAll();
    }

    @Override
    public void register(Long circleId, Member member) {
        Circle circle = circleRepository.findById(circleId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found"));
        CircleMember circleMember = new CircleMember();
        circleMember.setMember(member);
        circleMember.setCircle(circle);
        circleMember.setSkipCount(3);
        circleMember.setRole(CircleMember.Role.MEMBER);
        circleMemberRepository.save(circleMember);

    }

    @Override
    public void assignLeader(Long circleId, Long targetMemberId, Long actorMemberId) {
        CircleMember actor = circleMemberRepository.findByCircleIdAndMemberId(circleId, actorMemberId)
                .orElseThrow(() -> new IllegalStateException("서클 멤버가 아닙니다."));

        if (actor.getRole() != CircleMember.Role.LEADER) {
            throw new IllegalStateException("리더만 권한을 위임할 수 있습니다.");
        }

        CircleMember target = circleMemberRepository.findByCircleIdAndMemberId(circleId, targetMemberId)
                .orElseThrow(() -> new IllegalStateException("위임 대상이 서클 멤버가 아닙니다."));

        if (target.getMember().getId().equals(actorMemberId)) {
            throw new IllegalStateException("자기 자신에게 리더를 위임할 수 없습니다.");
        }

        actor.setRole(CircleMember.Role.MEMBER);
        target.setRole(CircleMember.Role.LEADER);

        circleMemberRepository.save(actor);
        circleMemberRepository.save(target);
    }


    @Override
    public void kickMember(Long circleId, Long targetMemberId, Long actorMemberId) {
        CircleMember actor = circleMemberRepository.findByCircleIdAndMemberId(circleId, actorMemberId)
                .orElseThrow(() -> new IllegalStateException("서클 멤버가 아닙니다."));

        if (actor.getRole() != CircleMember.Role.LEADER) {
            throw new IllegalStateException("리더만 멤버를 추방할 수 있습니다.");
        }

        CircleMember target = circleMemberRepository.findByCircleIdAndMemberId(circleId, targetMemberId)
                .orElseThrow(() -> new IllegalStateException("추방 대상이 서클 멤버가 아닙니다."));

        if (target.getRole() == CircleMember.Role.LEADER) {
            throw new IllegalStateException("다른 리더는 추방할 수 없습니다.");
        }
        routineService.saveAsPersonalRoutine(targetMemberId, circleId);
        circleMemberRepository.delete(target);
    }

    @Override
    public void leaveCircle(Long circleId, Long memberId, Long actorMemberId) {
        if (!memberId.equals(actorMemberId)) {
            throw new IllegalStateException("본인만 탈퇴할 수 있습니다.");
        }

        CircleMember member = circleMemberRepository.findByCircleIdAndMemberId(circleId, memberId)
                .orElseThrow(() -> new IllegalStateException("서클 멤버가 아닙니다."));

        if (member.getRole() == CircleMember.Role.LEADER) {
            throw new IllegalStateException("리더는 탈퇴할 수 없습니다. 리더 권한을 위임하세요.");
        }
        routineService.saveAsPersonalRoutine(actorMemberId, circleId);
        circleMemberRepository.delete(member);

    }
}
