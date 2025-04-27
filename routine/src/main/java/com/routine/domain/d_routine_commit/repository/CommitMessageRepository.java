package com.routine.domain.d_routine_commit.repository;


import com.routine.domain.d_routine_commit.model.CommitMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommitMessageRepository extends JpaRepository<CommitMessage, Long> {

    // 존재하는 커밋 메세지 찾기
    Optional<CommitMessage> findByMemberIdAndRoutineIdAndCommitDate(Long memberId, Long routineId, LocalDate commitDate);

    // 특정 루틴에 대한 모든 커밋 메세지 출력
    List<CommitMessage> findByRoutineIdOrderByCommitDateDesc(Long routineId);
}
