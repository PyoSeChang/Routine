package com.routine.domain.a_member.repository;


import com.routine.domain.a_member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
     Member findByLoginId(String loginId);
}
