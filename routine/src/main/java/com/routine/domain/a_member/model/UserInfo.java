package com.routine.domain.a_member.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    private String email;
    private String occupation;   // 직업
    private String status;       // 신분 (ex. 학생, 직장인 등)
    private String tags;         // 관심사 태그 (예: "운동,공부,습관")
    private int totalPoints;
}
