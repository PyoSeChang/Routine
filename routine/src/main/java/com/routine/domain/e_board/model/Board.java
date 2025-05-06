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
    private BoardType type; // 공지, 후기, 홍보 등

    @Enumerated(EnumType.STRING)
    private Category category; //

    @Enumerated(EnumType.STRING)
    private DetailCategory detailCategory;

    private String tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private BoardStatus status;

}
