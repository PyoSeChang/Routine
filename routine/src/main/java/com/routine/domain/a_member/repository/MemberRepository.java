package com.routine.domain.a_member.repository;


import com.routine.domain.a_member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인 아이디 중복 여부 확인
    boolean existsByLoginId(String loginId);

    // 닉네임 중복 여부 확인
    boolean existsByNickname(String nickname);

     Optional<Member> findByLoginId(String loginId);

     @Query("SELECT m.nickname FROM Member m WHERE m.id = :memberId")
     String findNicknameById(@Param("memberId") Long memberId);



}
