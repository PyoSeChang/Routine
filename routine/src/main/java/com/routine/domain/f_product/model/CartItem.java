package com.routine.domain.f_product.model;

import com.routine.domain.a_member.model.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    //장바구니에 담은 개수
    private int quantity;

    public int getQuantity() {

        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // 소계 (product price * quantity)
    @Column(nullable = false)
    private int subtotal;

    // 배송비
    @Column(nullable = false)
    private int shippingCost;

    // 총 가격 (subtotal + shippingCost)
    @Column(nullable = false)
    private int totalPrice;

    public void calculatePrices() {
        if (product != null) {
            this.subtotal = product.getPrice() * quantity;
            this.shippingCost = calculateShippingCost();
            this.totalPrice = subtotal + shippingCost;
        }
    }

    private int calculateShippingCost() {
        // 예시 로직: 3만원 이상이면 무료배송
        if (subtotal >= 30000) {
            return 0;
        } else {
            return 3000;
        }
    }

}
