package com.routine.domain.b_circle.repository;


import com.routine.domain.b_circle.dto.CircleSummaryDTO;
import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.b_circle.model.CircleMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CircleMemberRepository extends JpaRepository<CircleMember, Long> {

    List<CircleMember> findAllByCircleId(Long circleId);

    Optional<CircleMember> findByMemberIdAndCircleId(Long memberId, Long circleId);

    @Modifying
    @Query("UPDATE CircleMember cm SET cm.skipCount = 3")
    void updateSkipCountToThreeForAll();

    @Query("select cm.member.id from CircleMember cm where cm.circle.id = :circleId")
    List<Long> findMemberIdsByCircleId(@Param("circleId") Long circleId);


    boolean existsByCircleIdAndMemberIdAndRole(Long circleId, Long memberId, CircleMember.Role role);

    boolean existsByCircleIdAndMemberId(Long circleId, Long memberId);

    @EntityGraph(attributePaths = {"circle"})
    List<CircleMember> findAllByMemberId(Long memberId);

    int countByCircleId(Long id);

    @Query("SELECT cm.member.id FROM CircleMember cm WHERE cm.circle.id = :circleId AND cm.role = 'LEADER'")
    Optional<Long> findAdminIdByCircleId(Long circleId);

    Optional<CircleMember> findByCircleIdAndMemberId(Long circleId, Long actorMemberId);
}
