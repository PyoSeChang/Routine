package com.routine.domain.a_member.repository;


import com.routine.domain.a_member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
     Member findByLoginId(String loginId);

     @Query("SELECT m.nickname FROM Member m WHERE m.id = :memberId")
     String findNicknameById(@Param("memberId") Long memberId);

}
