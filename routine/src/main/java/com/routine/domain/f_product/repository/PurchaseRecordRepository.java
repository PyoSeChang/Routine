package com.routine.domain.f_product.repository;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.f_product.model.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, Long> {

    List<PurchaseRecord> findByMember_Id(Long memberId);


}
