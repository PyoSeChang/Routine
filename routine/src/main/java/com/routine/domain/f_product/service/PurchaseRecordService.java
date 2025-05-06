package com.routine.domain.f_product.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.f_product.dto.PurchaseRecordDTO;
import com.routine.domain.f_product.model.CartItem;

import java.util.List;

public interface PurchaseRecordService {
    //구매내역을 리스트형태로 반환 / 사용자이름으로 구매기록 조회/ 로그인한 사용자의  username
    List<PurchaseRecordDTO> getPurchaseRecordsByBuyer(Member member);


    //구매 기록으로 저장
    void confirmPurchase(List<CartItem> cartItems, Member member);




}
