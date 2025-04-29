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
}
