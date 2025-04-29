package com.routine.domain.b_circle.dto;

import com.routine.domain.c_routine.dto.TaskDTO;
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
        private String nickname;
        private Double commitRate;
        private List<TaskDTO> tasks;
        private String commitMessage;
    }
}
