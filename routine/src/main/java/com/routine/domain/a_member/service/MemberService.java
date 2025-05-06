package com.routine.domain.a_member.service;

import com.routine.domain.a_member.dto.AdminPointLogDTO;
import com.routine.domain.a_member.dto.RegisterRequestDTO;
import com.routine.domain.a_member.dto.UserInfoUpdateRequest;
import com.routine.domain.a_member.model.Member;
import com.routine.domain.d_routine_commit.dto.PointLogDTO;

import java.util.List;

public interface MemberService {
    boolean isLoginIdDuplicate(String loginId);

    boolean isNicknameDuplicate(String nickname);

    void register (RegisterRequestDTO dto);

    void userInfoUpdate (String loginId, UserInfoUpdateRequest request);

    List<AdminPointLogDTO> getPointLogsByLoginId(String loginId);
}
