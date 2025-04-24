package com.routine.domain.d_routine_commit.model;

import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.a_member.model.Member;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name= "commit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate commitDate;

    @Enumerated(EnumType.STRING)
    private CommitStatus status;

}
