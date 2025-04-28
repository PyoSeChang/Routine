package com.routine.domain.d_routine_commit.dto;

import com.routine.domain.d_routine_commit.model.PointLog;
import com.routine.domain.d_routine_commit.model.enums.PointReason;
import com.routine.domain.d_routine_commit.model.enums.PointLogStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PointLogDTO {

    private Long id;
    private int amount;
    private String reason; //
    private String remark; //
    private LocalDateTime createdAt;

    public static PointLogDTO from(PointLog pointLog, String routineName) {
        return PointLogDTO.builder()
                .id(pointLog.getId())
                .amount(pointLog.getAmount())
                .reason(translateReason(pointLog.getReason())) // 한글 번역
                .remark(makeRemark(pointLog, routineName))     // 비고 작성
                .createdAt(pointLog.getCreatedAt())
                .build();
    }

    private static String translateReason(PointReason reason) {
        return switch (reason) {
            case CIRCLE_ROUTINE_COMMIT -> "루틴 커밋 보상";
            case CIRCLE_ROUTINE_COLLECTIVE_BONUS -> "서클 성과 보너스";
            case PURCHASE_PRODUCTS -> "상품 구매";
        };
    }

    private static String makeRemark(PointLog pointLog, String routineName) {
        if (pointLog.getReason() == PointReason.PURCHASE_PRODUCTS) {
            return "구매 ID: " + pointLog.getPurchaseId();
        } else {
            return pointLog.getCommitDate() + " / " + routineName;
        }
    }
}
