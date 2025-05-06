package com.routine.domain.f_product.model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Entity(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 증가
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private int price;

    @Lob
    private String description;

    @NotNull
    private String tags; // 예: "자기계발,공부,루틴"

    private String image; // 예: "/images/product/product1.jpg"

    private String category; // 카테고리 분류

    // 카테고리 getter, setter 추가
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
