package com.routine.domain.d_routine_commit.model;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import jakarta.persistence.*;
import lombok.*;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task_commit_rate",
        indexes = {
                @Index(name = "idx_task_commit_routine_member", columnList = "routine_id, member_id"),
                @Index(name = "idx_task_commit_year_week", columnList = "year, week"),
                @Index(name = "idx_task_commit_date", columnList = "commit_date")
        })
public class TaskCommitRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 루틴
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    // 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 태스크 식별 (루틴 내 taskId)
    @Column(nullable = false)
    private Long taskId;

    // 주차 기록용
    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer week;

    // 하루 기록용 (null 가능)
    private LocalDate commitDate;

    // 하루: SUCCESS / FAIL / SKIP
    @Enumerated(EnumType.STRING)
    private CommitStatus commitStatus;

    // 주차 요약용: 0.0 ~ 1.0 이행률
    private Double taskCommitRate;
}
