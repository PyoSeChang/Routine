package com.routine.domain.f_product.dto;

import com.routine.domain.f_product.model.Product;

import java.util.List;

public class CartDTO {

//  장바구니에 담긴 상품 목록
    List<ProductDTO> products;

    private int totalQuantity;
    private int subtotal;
    private int shippingCost;
    private int totalPrice;
    private int points;

}
