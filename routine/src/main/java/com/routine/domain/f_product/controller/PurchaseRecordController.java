package com.routine.domain.f_product.controller;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.a_member.service.MemberService;
import com.routine.domain.f_product.dto.PurchaseRecordDTO;
import com.routine.domain.f_product.model.CartItem;
import com.routine.domain.f_product.repository.CartItemRepository;
import com.routine.domain.f_product.service.PurchaseRecordService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class PurchaseRecordController {

    private final PurchaseRecordService purchaseRecordService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CartItemRepository cartItemRepository;
    private Member Member;

    @GetMapping("/order")
    public String listOrders(Model model, @AuthenticationPrincipal PrincipalDetails principal) {

        Member member = (principal != null) ? principal.getMember() : null; // ✅ principal이 null이면 "Guest"로
        List<PurchaseRecordDTO> orders = purchaseRecordService.getPurchaseRecordsByBuyer(member); // 주문 목록 비어 있어도 OK

        model.addAttribute("username", member.getLoginId()); // 화면에 username 넘김
        model.addAttribute("orders", orders);      // 주문 리스트 넘김
        return "cart/order"; // templates/cart/order.html 열기
    }

    @GetMapping("/purchase")
    public String confirmPurchase(Model model, @AuthenticationPrincipal PrincipalDetails principal) {

        if (principal == null) {
            return "redirect:/member/login";
        }

        Member member = principal.getMember();
        List<CartItem> cartItems = cartItemRepository.getItemsByMember((member));
        purchaseRecordService.confirmPurchase(cartItems, member);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("member", member);


        return "redirect:/cart/order";
    }
}




