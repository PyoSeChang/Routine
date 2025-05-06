package com.routine.domain.a_member.service;


import com.routine.domain.a_member.dto.AdminPointLogDTO;
import com.routine.domain.a_member.dto.RegisterRequestDTO;
import com.routine.domain.a_member.dto.UserInfoUpdateRequest;
import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.model.Role;
import com.routine.domain.a_member.model.UserInfo;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.model.PointLog;
import com.routine.domain.d_routine_commit.repository.PointLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PointLogRepository pointLogRepository;
    private final RoutineRepository routineRepository;

    @Override
    public boolean isLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean isNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }


    @Override
    public void register(RegisterRequestDTO dto) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        Member member = Member.builder()
                .loginId(dto.getLoginId())
                .password(encodedPassword)
                .nickname(dto.getNickname())
                .role(Role.USER)
                .build();

        UserInfo userInfo = UserInfo.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .todayAvailablePoint(50)
                .todayAvailablePointDate(LocalDate.now())
                .build();

        userInfo.setMember(member);
        member.setUserInfo(userInfo);

        memberRepository.save(member); // 이거 한 줄이면 같이 persist됨



    }

    @Override
    public void userInfoUpdate(String loginId, UserInfoUpdateRequest request) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 비밀번호 검증
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String encoded = passwordEncoder.encode(request.getPassword());
            member.setPassword(encoded);
        }

        // 닉네임 변경
        member.setNickname(request.getNickname());

        // UserInfo 필드 변경
        UserInfo info = member.getUserInfo();
        if (info != null) {
            info.setEmail(request.getEmail());
        }

        // 변경된 내용 저장
        memberRepository.save(member);
    }

    @Override
    public List<AdminPointLogDTO> getPointLogsByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("해당 로그인 ID를 가진 사용자가 없습니다: " + loginId));

        List<PointLog> logs = pointLogRepository.findByMember(member);

        return logs.stream()
                .map(log -> {
                    String routineName = "";
                    if (log.getRoutineId() != null) {
                        routineName = routineRepository.findById(log.getRoutineId())
                                .map(Routine::getTitle)
                                .orElse("루틴 없음");
                    }
                    return AdminPointLogDTO.from(log, routineName);
                })
                .collect(Collectors.toList());
    }



}
