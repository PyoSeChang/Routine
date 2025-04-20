package com.routine.domain.f_product.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    private String tags; // 예: "자기계발,공부,루틴"

    private String imageUrl; // 예: "/images/product/product1.jpg"
}
