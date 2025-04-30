package com.routine.domain.b_circle.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CircleResponse {
    private CircleRoutineDTO circleRoutine;
    private CircleRoutineCommits memberCommits;
    private AuthorizationDTO loginMember;
    private List<CircleMemberDTO> circleMembers;
}
