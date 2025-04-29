package com.routine.domain.c_routine.model;

import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.a_member.model.Member;
import com.routine.domain.e_board.model.Category;
import com.routine.domain.e_board.model.DetailCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category; //

    @Enumerated(EnumType.STRING)
    private DetailCategory detailCategory;
    private String tags;

    private String description;

    private boolean isGroupRoutine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "circle_id")
    private Circle circle;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "routine_repeat_days", joinColumns = @JoinColumn(name = "routine_id"))
    @Column(name = "repeat_day")
    private List<DayOfWeek> repeatDays;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutineTask> routineTasks = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
