package com.routine.domain.d_routine_commit.model;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "commit_rate",
        indexes = {
                @Index(name = "idx_routine_week", columnList = "routine_id, member_id, year, week"),
                @Index(name = "idx_commit_date", columnList = "commit_date") // ✅ 추가 인덱스
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

    private Integer year;               // 주차용
    private Integer week;               // 주차용
    private LocalDate commitDate;       // ✅ 일일 이행률 기록
    private double commitRate;          // ✅ 0.0 ~ 1.0
    private boolean skipped;
}
