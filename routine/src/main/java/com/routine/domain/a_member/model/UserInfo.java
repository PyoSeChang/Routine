package com.routine.domain.a_member.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String name;
    private String email;
    private String phone;
    private String tags;         // 관심사 태그 (예: "운동,공부,습관")
    private int totalPoints;

    private int todayAvailablePoint;
    private LocalDate todayAvailablePointDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;

    @PrePersist
    protected void onCreate() {
        this.regDate = LocalDateTime.now();
    }
}
