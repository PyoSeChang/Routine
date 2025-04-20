package com.routine.domain.e_board.model;



import com.routine.domain.a_member.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "report_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 신고일 경우
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_status_id")
    private BoardStatus boardStatus;

    // 댓글 신고일 경우
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @Enumerated(EnumType.STRING)
    private ReportType reason;

    private String detail;

    private LocalDateTime reportedAt;

    @PrePersist
    protected void onReport() {
        this.reportedAt = LocalDateTime.now();
    }

}
