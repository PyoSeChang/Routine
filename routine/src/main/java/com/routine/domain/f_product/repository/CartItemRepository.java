package com.routine.domain.f_product.repository;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.f_product.model.CartItem;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProductId(Long productId);

    List<CartItem> getItemsByMember(Member member);

    List<CartItem> findByMemberId(Long memberId);

    int countByProductId(Long productId);
}
