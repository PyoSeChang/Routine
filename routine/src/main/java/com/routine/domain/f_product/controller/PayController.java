package com.routine.domain.f_product.controller;

import com.routine.domain.f_product.model.CartItem;
import com.routine.domain.f_product.service.CartService;
import com.routine.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Log4j2
@RequestMapping("/cart")
@Controller
@RequiredArgsConstructor
public class PayController {
    private final CartService cartService;


//    Long memberId = 1L;

    @GetMapping("/pay")
    public String showPayPage(Model model,
                              @AuthenticationPrincipal PrincipalDetails principal) {
        Long memberId = principal.getMember().getId();


        List<CartItem> cartItems = cartService.getCartItems(memberId);

        // ⚠️ 각 상품의 subtotal 계산
        for (CartItem item : cartItems) {
            item.calculatePrices();
        }

        int subtotal = cartService.calculateSubtotal(cartItems);
        int shippingCost = cartService.calculateShipping(cartItems);
        int totalPrice = cartService.calculateTotal(cartItems);

        model.addAttribute("cartItems", cartItems);          // ✅ 장바구니 항목 전달
        model.addAttribute("subtotal", subtotal);            // ✅ 상품 총합
        model.addAttribute("shippingCost", shippingCost);    // ✅ 배송비
        model.addAttribute("totalPrice", totalPrice);        // ✅ 결제금액

        return "/cart/pay";
    }


    @PostMapping("/confirm")
    public String confirmPayment(@RequestParam String receiverName,
                                 @RequestParam String address,
                                 @RequestParam String phone,
                                 @RequestParam String paymentMethod,
                                 @RequestParam int usedPoints,
                                 RedirectAttributes redirectAttributes) {

        // Flash로 값 전달
        redirectAttributes.addFlashAttribute("receiverName", receiverName);
        redirectAttributes.addFlashAttribute("address", address);
        redirectAttributes.addFlashAttribute("phone", phone);
        redirectAttributes.addFlashAttribute("paymentMethod", paymentMethod);
        redirectAttributes.addFlashAttribute("usedPoints", usedPoints);
//        redirectAttributes.addFlashAttribute("earnedPoints", (int) (30000 * 0.1)); // 예시

        return "redirect:/cart/confirm";
    }
// member_id가 있을때 하게함
//    @PostMapping("/cart/confirm")
//    public String confirmPayment(@RequestParam String receiverName,
//                                 @RequestParam String address,
//                                 @RequestParam String phone,
//                                 @RequestParam String paymentMethod,
//                                 @RequestParam int usedPoints,
//                                 Principal principal) {
//        // 예시 데이터: 실제론 서비스에서 cartItem, price 계산해야 함
//        Long memberId = memberService.getMemberIdByUsername(principal.getName());
//
//        int subtotal = cartItemService.calculateSubtotalByMemberId(memberId);
//        int shippingCost = cartItemService.calculateShippingByMemberId(memberId);
//        int totalPrice = subtotal + shippingCost - usedPoints;
//
//        // ✅ 결제 금액의 10%를 포인트로 적립
//        int earnedPoints = (int) (totalPrice * 0.10); // 소수점 버림
//
//        // ✅ 기존 포인트 + 적립 포인트 저장
//        memberService.addPoints(memberId, earnedPoints - usedPoints); // 사용한 포인트는 차감
//
//        // ✅ 주문 저장 등 처리
//        orderService.saveOrder(memberId, totalPrice, receiverName, address, phone, paymentMethod);
//
//        return "redirect:/pay/success";
//    }


    @GetMapping("/confirm")
    public String showConfirmPage(Model model,
                                  @ModelAttribute("receiverName") String receiverName,
                                  @ModelAttribute("address") String address,
                                  @ModelAttribute("phone") String phone,
                                  @ModelAttribute("paymentMethod") String paymentMethod,
                                  @AuthenticationPrincipal PrincipalDetails principal) {
//                                  @ModelAttribute("usedPoints") int usedPoints,
//                                  @ModelAttribute("earnedPoints") int earnedPoints)

        Long memberId = principal.getMember().getId();
        List<CartItem> cartItems = cartService.getCartItems(memberId);
        int subtotal = cartService.calculateSubtotal(cartItems);
        int shippingCost = cartService.calculateShipping(cartItems);
        int totalPrice = cartService.calculateTotal(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shippingCost", shippingCost);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("receiverName", receiverName);
        model.addAttribute("address", address);
        model.addAttribute("phone", phone);
        model.addAttribute("paymentMethod", paymentMethod);
//        model.addAttribute("usedPoints", usedPoints);
//        model.addAttribute("earnedPoints", earnedPoints);

        return "/cart/confirm";
    }
}


