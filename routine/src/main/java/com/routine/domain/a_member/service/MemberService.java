package com.routine.domain.a_member.service;

import com.routine.domain.a_member.dto.RegisterRequestDTO;
import com.routine.domain.a_member.model.Member;

public interface MemberService {
    void regiser (RegisterRequestDTO dto);
}
