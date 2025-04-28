package com.routine.domain.a_member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDTO {
    private Long memberId;
    private String nickname;


}
