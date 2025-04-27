package com.routine.domain.d_routine_commit.model;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "commit_message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate commitDate; // 커밋 날짜

    @Column(length = 500)
    private String message; // 작성한 커밋 메세지

    private Boolean isPublic; // 공개 여부 (서클에 공유할지)

}

