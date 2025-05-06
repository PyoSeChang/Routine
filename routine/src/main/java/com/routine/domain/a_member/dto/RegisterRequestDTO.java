package com.routine.domain.a_member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String loginId;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String email;
}
