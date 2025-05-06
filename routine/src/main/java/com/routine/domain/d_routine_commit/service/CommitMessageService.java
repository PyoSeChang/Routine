package com.routine.domain.d_routine_commit.service;

import com.routine.domain.c_routine.dto.MessageDTO;
import com.routine.domain.d_routine_commit.dto.CommitMessageDTO;

import java.time.LocalDate;
import java.util.List;

public interface CommitMessageService {
    // 커밋 메세지 저장하기
    void saveCommitMessage(Long memberId, Long routineId, LocalDate commitDate, String message, Boolean isPublic);

    // 서클 커밋 메세지 모아보기
    List<CommitMessageDTO> getMessagesByRoutine(Long routineId);

    // 나의 커밋 메세지 모아보기
    List<MessageDTO> getMyMessages(Long routineId);
}
