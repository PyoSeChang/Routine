package com.routine.domain.c_routine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
public class CircleRoutineCommits {

    private List<MemberCommitInfo> memberCommits; // 전체 멤버 커밋 기록 리스트

    @Data
    @Builder
    @AllArgsConstructor
    public static class MemberCommitInfo {
        private Long memberId;
        private String commitMessage; // 커밋 메세지
        private Double commitRate;    // 이행률 (성공률)
        private List<TaskDTO> tasks;  // 커밋한 태스크들
    }
}
