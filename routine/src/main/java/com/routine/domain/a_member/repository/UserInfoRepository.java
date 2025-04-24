package com.routine.domain.a_member.repository;


import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
     Optional<UserInfo> findByMemberId(Long MemberId);
}
