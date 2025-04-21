package com.routine.domain.a_member.controller;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.model.Role;
import com.routine.domain.a_member.service.MemberService;
import com.routine.domain.a_member.dto.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String displayLoginPage() {
        return "view/login";
    }

    @GetMapping("/main")
    public String displayMainPage() {
        return "view/main";
    }

    @GetMapping("/register")
    public String displaySignupPage() {
        return "view/register";
    }

    @PostMapping("/register")
    public String register(RegisterRequestDTO dto) {
        memberService.regiser(dto);
        return "redirect:/member/login";
    }

//    @GetMapping("/profile")
//    public String displayProfilePage(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
//        model.addAttribute("userInfo", principal.getMember().getUserInfo());
//        return "view/profile";
//    }
//
//    @PostMapping("/profile")
//    public String updateUserInfo(UserInfoDTO userInfoDto, @AuthenticationPrincipal PrincipalDetails principal) {
//        memberService.updateUserInfo(principal.getMember().getId(), userInfoDto);
//        return "redirect:/member/profile";
//    }
}
