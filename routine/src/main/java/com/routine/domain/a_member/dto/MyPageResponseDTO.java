package com.routine.domain.a_member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageResponseDTO {
    private Long memberId;
    private int myPoints;
    private String nickname;
    private String email;

    public MyPageResponseDTO(Long memberId, int myPoints, String nickname, String email) {
        this.memberId = memberId;
        this.myPoints = myPoints;
        this.nickname = nickname;
        this.email = email;
    }

    // getters, setters
}
