package com.routine.domain.f_product.controller;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.f_product.model.CartItem;
import com.routine.domain.f_product.repository.CartItemRepository;
import com.routine.domain.f_product.service.CartService;
import com.routine.security.model.PrincipalDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Log4j2
@RequestMapping("/cart")
@Controller
@RequiredArgsConstructor

public class CartController {

    //장바구니 CartService를 빈으로 주입받음
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemRepository cartItemRepository;


    @PostMapping("/cinsert")
    public String addItemToCart(@RequestParam Long productId,
                                @RequestParam int quantity,
                                @RequestParam int price,
                                @AuthenticationPrincipal PrincipalDetails principal,
                                HttpSession session) {


        Member member = principal.getMember();
        cartService.addItemToCart(productId,quantity, member);

        return "redirect:/cart/clist";
    }




    // 장바구니 페이지

    @GetMapping("/clist")
    public String viewCart(Model model,
                           @AuthenticationPrincipal PrincipalDetails principal) {
        Long memberId = principal.getMember().getId();
        List<CartItem> cartItems = cartService.getCartItems(memberId);  // cartService로부터 장바구니 아이템을 가져옴
        int subtotal = cartService.calculateSubtotal(cartItems);  // 서브토탈 계산
        int shippingCost = cartService.calculateShipping(cartItems);  // 배송비 계산
        int totalPrice = cartService.calculateTotal(cartItems); // 총 가격 계산


        model.addAttribute("cartItems",cartItems);  // 장바구니 아이템
        model.addAttribute("subtotal", subtotal);  // 서브토탈
        model.addAttribute("shippingCost", shippingCost);  // 배송비
        model.addAttribute("totalPrice", totalPrice);  // 총 가격

        return "cart/clist";
    }

    @PostMapping("/delete/{id}")
    public String removeItemFromCart(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        cartItemRepository.deleteById(id);
        return "redirect:/cart/clist"; // 장바구니 페이지로 리다이렉트
    }



}
