package com.routine.domain.f_product.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.a_member.repository.MemberRepository;
import com.routine.domain.f_product.dto.PurchaseRecordDTO;
import com.routine.domain.f_product.model.CartItem;
import com.routine.domain.f_product.model.PurchaseRecord;
import com.routine.domain.f_product.repository.PurchaseRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseRecordServiceImpl implements PurchaseRecordService {

    private final PurchaseRecordRepository purchaseRecordRepository;

    @Override
    public List<PurchaseRecordDTO> getPurchaseRecordsByBuyer(Member member) {

        List<PurchaseRecord> records = purchaseRecordRepository.findByMember_Id(member.getId());

        return records.stream()
                .map(record -> new PurchaseRecordDTO(
                        record.getProduct().getTitle(),
                        record.getProduct().getPrice(),
                        record.getQuantity(),
                        record.getPurchasedAt(),
                        record.getDeliveryStatus()
                ))
                .collect(Collectors.toList());
    }

    // ✅ 장바구니 항목을 구매 기록으로 저장
    @Override
    public void confirmPurchase(List<CartItem> cartItems, Member member) {
        List<PurchaseRecord> purchases = cartItems.stream()
                .map(item -> PurchaseRecord.builder()
                        .member(member)
                        .product(item.getProduct())
                        .quantity(item.getQuantity())
                        .deliveryStatus("배송 준비중") // 초기 상태
                        .build()
                )
                .collect(Collectors.toList());

        purchaseRecordRepository.saveAll(purchases); // 여러 건 한 번에 저장
    }
}