package com.routine.domain.d_routine_commit.model;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 루틴 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 어떤 루틴에 대한 임시 상태인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    // 오늘 날짜 루틴 기준
    private LocalDate targetDate;

    // 체크된 태스크들 (태스크 이름 기준으로 저장)
    @OneToMany(mappedBy = "commitDraft", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskCheckStatus> taskStatuses;

    // 상태: 저장 중인지, 제출 완료인지
    @Enumerated(EnumType.STRING)
    private RoutineCommitStatus status; // DRAFT or SUBMITTED

    // 저장 시각
    private LocalDateTime savedAt;

    @PrePersist
    protected void onCreate() {
        this.savedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.savedAt = LocalDateTime.now();
    }
}
