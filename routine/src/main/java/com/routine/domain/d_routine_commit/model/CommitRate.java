package com.routine.domain.d_routine_commit.model;

import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.a_member.model.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "commit_rate",
        indexes = {
                @Index(name = "idx_routine_week", columnList = "routine_id, member_id, year, week")
        })
public class CommitRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private int year;         // ISO 기준 연도
    private int week;         // ISO 기준 주차

    private double commitRate; // 0.0 ~ 1.0 (이행률 비율)
}
