package com.routine.domain.a_member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoUpdateRequest {
    private String nickname;
    private String email;
    private String password;
    private String confirmPassword;
}
