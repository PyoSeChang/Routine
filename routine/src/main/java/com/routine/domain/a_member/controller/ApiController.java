package com.routine.domain.a_member.controller;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.dto.MemberDTO;
import com.routine.security.model.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/members/me")
    public MemberDTO getCurrentMember(
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        Member m = principal.getMember();
        return MemberDTO.builder()
                .memberId(m.getId())
                .nickname(m.getNickname())

                .build();
    }
}
