package com.routine.domain.e_board.model;

import com.routine.domain.a_member.model.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardType type;      // 공지, 후기, 홍보 등

    private String category;     // 글 주제 (예: 공부, 루틴, 독서 등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @OneToOne(mappedBy = "board", orphanRemoval = true)
    private BoardStatus status;
}
