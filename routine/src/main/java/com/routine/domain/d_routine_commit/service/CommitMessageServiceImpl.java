package com.routine.domain.d_routine_commit.service;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.a_member.service.MemberService;
import com.routine.domain.c_routine.dto.MessageDTO;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.dto.CommitMessageDTO;
import com.routine.domain.d_routine_commit.model.CommitMessage;
import com.routine.domain.d_routine_commit.repository.CommitMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CommitMessageServiceImpl implements CommitMessageService {

    private final CommitMessageRepository commitMessageRepository;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private static final Random random = new Random();

    @Override
    public void saveCommitMessage(Long memberId, Long routineId, LocalDate commitDate, String message, Boolean isPublic) {
        CommitMessage commitMessage = commitMessageRepository
                .findByMemberIdAndRoutineIdAndCommitDate(memberId, routineId, commitDate)
                .orElseGet(() -> {
                    // 없으면 새로 생성
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
                    Routine routine = routineRepository.findById(routineId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 루틴입니다."));

                    return CommitMessage.builder()
                            .member(member)
                            .routine(routine)
                            .commitDate(commitDate)
                            .build();
                });

        // 값 덮어쓰기
        commitMessage.setMessage(message);
        commitMessage.setIsPublic(isPublic);

        commitMessageRepository.save(commitMessage);
    }

    @Override
    public List<CommitMessageDTO> getMessagesByRoutine(Long routineId) {
        List<CommitMessage> messages = commitMessageRepository.findByRoutineIdOrderByCommitDateDesc(routineId);

        return messages.stream()
                .map(CommitMessageDTO::fromEntity)
                .toList();
    }

    @Override
    public List<MessageDTO> getMyMessages(Long routineId) {
        return commitMessageRepository.findByRoutineIdOrderByCommitDateAsc(routineId).stream()
                .map(MessageDTO::fromEntity)
                .toList();
    }

    public static int generateColorIdExceptFor1() {
        int rand = random.nextInt(90); // 0~89 (총합 90 기준)

        if (rand < 40) return 1;
        else if (rand < 50) return 2;
        else if (rand < 60) return 3;
        else if (rand < 70) return 4;
        else if (rand < 80) return 5;
        else return 6;
    }

}
