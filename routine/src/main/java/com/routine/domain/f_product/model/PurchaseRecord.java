package com.routine.domain.f_product.model;

import com.routine.domain.a_member.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 - 여러 구매 기록이 한 명의 회원에게 귀속
    @JoinColumn(name = "member_id") //즉, 구매자는  member객체로 연결
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; //product`는 `Product` 엔티티 참조

    private int quantity;

    private LocalDateTime purchasedAt;

    @PrePersist
    protected void onPurchase() {
        this.purchasedAt = LocalDateTime.now();
        if (this.deliveryStatus == null) {
            this.deliveryStatus = "배송 준비중"; // 기본값

        }

    }


    //배송상태
    private String deliveryStatus;



}
