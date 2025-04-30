package com.routine.domain.c_routine.service;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.b_circle.repository.CircleMemberRepository;
import com.routine.domain.b_circle.repository.CircleRepository;
import com.routine.domain.c_routine.dto.RoutineDTO;
import com.routine.domain.c_routine.dto.RoutineSummary;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.model.RoutineTask;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.c_routine.repository.RoutineTaskRepository;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final RoutineTaskRepository routineTaskRepository;
    private final CircleRepository circleRepository;
    private final CircleMemberRepository circleMemberRepository;
    private final CommitLogRepository commitLogRepository;

    @Transactional
    @Override
    public Routine saveRoutine(RoutineDTO routineDTO, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Routine routine = routineDTO.toPersonalRoutine(member);


        routineRepository.save(routine);

        List<RoutineTask> routineTasks = routineDTO.toRoutineTasks(routine);
        if (!routineTasks.isEmpty()) {
            routineTaskRepository.saveAll(routineTasks);
        }
        routine.setRoutineTasks(routineTasks);

        LocalDate today = LocalDate.now();
        routine.getRoutineTasks().forEach(task -> {
            boolean exists = commitLogRepository.existsByRoutineAndMemberAndTaskAndCommitDate(routine, member, task, today);
            if (!exists) {
                CommitLog log = CommitLog.builder()
                        .routine(routine)
                        .member(member)
                        .task(task)
                        .commitDate(today)
                        .status(CommitStatus.NONE)
                        .build();
                commitLogRepository.save(log);
            }
        });
        return routine;
    }

    @Transactional
    @Override
    public RoutineDTO getRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 루틴입니다."));

        List<RoutineTask> routineTasks = routineTaskRepository.findAllByRoutine(routine);

        return RoutineDTO.fromEntity(routine, routineTasks);
    }

    @Transactional
    @Override
    public void updateRoutine(RoutineDTO routineDTO, Long routineId) {
        Routine original = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴이 존재하지 않습니다."));

        if (original.isGroupRoutine()) {
            throw new UnsupportedOperationException("그룹 루틴은 수정할 수 없습니다.");
        }

        Member member = original.getMember(); // 기존 멤버 유지
        Routine updated = routineDTO.toPersonalRoutine(member);
        updated.setId(original.getId()); // PK 유지

        routineTaskRepository.deleteAllByRoutine(original);

        routineRepository.save(updated);

        List<RoutineTask> tasks = routineDTO.toRoutineTasks(updated);
        if (!tasks.isEmpty()) {
            routineTaskRepository.saveAll(tasks);
        }
    }

    @Override
    public void deleteRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 루틴입니다."));

        if (routine.isGroupRoutine()) {
            throw new UnsupportedOperationException("그룹 루틴은 삭제할 수 없습니다.");
        }
        routineTaskRepository.deleteAllByRoutine(routine);
        routineRepository.delete(routine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoutineDTO> getAllRoutinesByMember(Long memberId) {
        List<Routine> routines = routineRepository.findAllByMemberId(memberId);

        return routines.stream()
                .map(routine -> {
                    List<RoutineTask> tasks = routineTaskRepository.findAllByRoutine(routine);
                    return RoutineDTO.fromEntity(routine, tasks);
                })
                .toList();
    }


    @Transactional
    @Override
    public void initializeTodayCommitLogs() {
        LocalDate today = LocalDate.now();

        List<Routine> routines = routineRepository.findAll();

        for (Routine routine : routines) {
            Member member = memberRepository.findById(routine.getMember().getId())
                    .orElseThrow(() -> new IllegalStateException("루틴에 연결된 멤버를 찾을 수 없습니다."));

            routine.getRoutineTasks().forEach(task -> {
                boolean exists = commitLogRepository.existsByRoutineAndMemberAndTaskAndCommitDate(routine, member, task, today);
                if (!exists) {
                    CommitLog log = CommitLog.builder()
                            .routine(routine)
                            .member(member)
                            .task(task)
                            .commitDate(today)
                            .status(CommitStatus.NONE)
                            .build();
                    commitLogRepository.save(log);
                }
            });
        }
    }

    @Override
    public List<RoutineSummary> getMyRoutinesSummary(Long memberId) {
        List<Routine> routines = routineRepository.findAllByMemberId(memberId);

        return routines.stream()
                .sorted(Comparator.comparing(Routine::isGroupRoutine).reversed())
                .map(routine -> RoutineSummary.builder()
                        .routineId(routine.getId())
                        .circleId(routine.isGroupRoutine() && routine.getCircle() != null ? routine.getCircle().getId() : null)
                        .routineName(routine.getTitle())
                        .repeatDays(routine.getRepeatDays())
                        .circleRoutine(routine.isGroupRoutine())
                        .createdAt(routine.getCreatedAt().toLocalDate())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void saveCircleRoutine(Long memberId, Long circleId) {
        // 0. 해당 서클의 리더 ID 찾기
        Long adminId = circleMemberRepository.findAdminIdByCircleId(circleId)
                .orElseThrow(() -> new IllegalArgumentException("리더가 존재하지 않는 서클입니다."));

        // 1. 리더의 서클 루틴 가져오기
        Routine origin = routineRepository.findByMemberIdAndCircleId(adminId, circleId)
                .orElseThrow(() -> new IllegalArgumentException("리더의 루틴이 존재하지 않습니다."));

        // 2. 루틴 복사  (groupRoutine = true)
        Routine newRoutine = Routine.builder()
                .title(origin.getTitle())
                .member(memberRepository.getReferenceById(memberId))
                .category(origin.getCategory())
                .detailCategory(origin.getDetailCategory())
                .repeatDays(new ArrayList<>(origin.getRepeatDays()))
                .tags(origin.getTags())
                .circle(circleRepository.getReferenceById(circleId))
                .isGroupRoutine(true)
                .build();
        routineRepository.save(newRoutine);

        // 3. 태스크 복사
        List<RoutineTask> tasks = routineTaskRepository.findAllByRoutineId(origin.getId());
        for (RoutineTask task : tasks) {
            // 3-1. RoutineTask 복사 및 저장
            RoutineTask copiedTask = routineTaskRepository.save(
                    RoutineTask.builder()
                            .routine(newRoutine)
                            .content(task.getContent())
                            .orderNumber(task.getOrderNumber())
                            .build()
            );

            // 3-2. CommitLog 생성 시 taskId 포함
            CommitLog commitLog = CommitLog.builder()
                    .routine(newRoutine)
                    .member(newRoutine.getMember())
                    .task(copiedTask)
                    .commitDate(LocalDate.now())
                    .status(CommitStatus.NONE)
                    .build();

            commitLogRepository.save(commitLog);
        }

    }


    @Override
    public void saveAsCircleRoutine(Long memberId, Long routineId, Long circleId) {
        System.out.println("--------------------routineId: " + routineId);
        Routine routine= routineRepository.findByIdAndMemberId(routineId, memberId);
        Circle circle = circleRepository.findById(circleId).orElse(null);
        routine.setCircle(circle);
        routine.setGroupRoutine(true);
        routineRepository.save(routine);

    }

    @Override
    public void saveAsPersonalRoutine(Long memberId, Long circleId) {
        Routine routine = routineRepository.findByMemberIdAndCircleId(memberId, circleId)
                .orElseThrow(() -> new IllegalArgumentException(" 루틴이 존재하지 않습니다."));
        routine.setCircle(null);
        routine.setGroupRoutine(false);
        routineRepository.save(routine);
    }


}
