package com.routine.domain.f_product.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.f_product.model.CartItem;

import java.util.List;

public interface CartService {
    // 장바구니에 상품 추가
    void addItemToCart(Long productId, int quantity, Member member);

    // 장바구니에서 상품 제거
    void removeItemFromCart(Long productId);

    // 장바구니 수량 변경
    void updateItemQuantity(Long productId, int quantity);

    // 장바구니의 모든 아이템 가져오기
    List<CartItem> getCartItems(Long memberId);

    // 장바구니 소계 계산
    int calculateSubtotal(List<CartItem> cartItems);

    // 배송비 계산 (고정 배송비를 사용)
    int calculateShipping(List<CartItem> cartItems);

    // 총 가격 계산
    int calculateTotal(List<CartItem> cartItems);
}
