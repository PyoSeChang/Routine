package com.routine.domain.d_routine_commit.model;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.d_routine_commit.model.enums.PointFailureReason;
import com.routine.domain.d_routine_commit.model.enums.PointLogStatus;
import com.routine.domain.d_routine_commit.model.enums.PointReason;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointLog {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PointReason reason;

    private Long routineId;

    @Enumerated(EnumType.STRING)
    private PointLogStatus status; //  SUCCESS, FAIL 구분

    @Enumerated(EnumType.STRING)
    private PointFailureReason failureReason; //  실패 사유 Enum. 성공 시 null

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); //  시:분:초까지 저장

    @Column(nullable = false)
    private LocalDate commitDate; // 포인트 지급의 기준이 되는 커밋 로그의 날짜


}

