package com.routine.domain.b_circle.model;


import com.routine.domain.e_board.model.Category;
import com.routine.domain.e_board.model.DetailCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Circle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String tags;

    private boolean isPublic;

    @Enumerated(EnumType.STRING)
    private Category category; //

    @Enumerated(EnumType.STRING)
    private DetailCategory detailCategory;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "circle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CircleMember> members;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
