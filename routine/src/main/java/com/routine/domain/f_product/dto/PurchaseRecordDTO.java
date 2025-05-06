package com.routine.domain.f_product.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchaseRecordDTO {
    private Long id; // 구매 기록 ID (구매한 순서번호)
    private String productTitle; // 상품명
    private int quantity; // 구매 수량
    private int price; // 상품 가격
    private LocalDateTime purchasedAt; // 구매 일시
    private String deliveryStatus;


    public PurchaseRecordDTO(String productTitle, int price, int quantity,
                             LocalDateTime purchasedAt, String deliveryStatus) {
        this.productTitle = productTitle;
        this.price = price;
        this.quantity = quantity;
        this.purchasedAt = purchasedAt;
        this.deliveryStatus = deliveryStatus;
    }


}
