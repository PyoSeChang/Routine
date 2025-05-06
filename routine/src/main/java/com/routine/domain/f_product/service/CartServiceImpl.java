package com.routine.domain.f_product.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.f_product.model.CartItem;
import com.routine.domain.f_product.model.Product;
import com.routine.domain.f_product.repository.CartItemRepository;
import com.routine.domain.f_product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {

    // 장바구니 아이템을 DB에서 관리하는 Repository
    @Autowired
    private CartItemRepository cartItemRepository;


    // 상품 정보를 관리하는 Repository
    @Autowired
    private ProductRepository productRepository;


    @Override
    public void addItemToCart(Long productId, int quantity, Member member) {
        log.info("member는-----"+member);
        // 상품 존재 여부 확인
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {

            Product p = product.get();
            // 실제 값 계산
            int subtotal = p.getPrice() * quantity;
            int shippingCost = 1000;
            int totalPrice = subtotal + shippingCost;



            // 장바구니 아이템 추가
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product.get());
            cartItem.setMember(member);
            cartItem.setQuantity(quantity);
            cartItem.setShippingCost(shippingCost);
            cartItem.setSubtotal(subtotal);
            cartItem.setTotalPrice(totalPrice);



            // 이미 장바구니에 해당 상품이 존재하는지 확인하고 수량을 증가시키는 로직 추가 가능
            cartItemRepository.save(cartItem);
        } else {
            throw new IllegalArgumentException("Product not found");
        }
                                                                                                                                                                                      
    }


    @Override
    public void removeItemFromCart(Long itemId) {
        // 장바구니에서 해당 상품 아이템 삭제
        CartItem cartItem = cartItemRepository.findById(itemId).orElse(null);
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
        } else {
            throw new IllegalArgumentException("Item not found in cart");
        }
    }

    @Override
    public void updateItemQuantity(Long productId, int quantity) {
        // 장바구니에서 해당 상품 아이템 수량 변경
        CartItem cartItem = cartItemRepository.findByProductId(productId);

        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            throw new IllegalArgumentException("Item not found in cart");
        }
    }

    @Override
    public List<CartItem> getCartItems(Long memberId) {
        return cartItemRepository.findByMemberId(memberId); // 장바구니에 저장된 모든 아이템 반환
        //추후 member별로 생길때  findall>> 변경해줘야함
    }

    @Override
    public int calculateSubtotal(List<CartItem> cartItems) {
        int subtotal = 0;
        for (CartItem cartItem : cartItems) {
            int price = cartItem.getProduct().getPrice();
            int quantity = cartItem.getQuantity();
            subtotal += price * quantity;
        }
        return subtotal;



    }

    @Override
    public int calculateShipping(List<CartItem> cartItems) {
        // 예시로 고정된 배송비 1,000원을 추가
        return 1000;
    }

    @Override
    public int calculateTotal(List<CartItem> cartItems) {
        int subtotal = calculateSubtotal(cartItems);
        int shipping = calculateShipping(cartItems);
        return subtotal + shipping;
    }
}