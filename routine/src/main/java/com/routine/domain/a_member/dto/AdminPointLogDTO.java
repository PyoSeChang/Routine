package com.routine.domain.a_member.dto;

import com.routine.domain.d_routine_commit.model.PointLog;
import com.routine.domain.d_routine_commit.model.enums.PointReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AdminPointLogDTO {

    private Long id;
    private String loginId;
    private int amount;
    private String reason;
    private String remark;
    private String status;
    private String failureReason;
    private Long routineId;
    private Long purchaseId;
    private LocalDateTime createdAt;
    private LocalDate commitDate;

    public static AdminPointLogDTO from(PointLog pointLog, String routineName) {
        return AdminPointLogDTO.builder()
                .id(pointLog.getId())
                .loginId(pointLog.getMember().getLoginId())
                .amount(pointLog.getAmount())
                .reason(translateReason(pointLog.getReason()))
                .remark(makeRemark(pointLog, routineName))
                .status(pointLog.getStatus().name())
                .failureReason(pointLog.getFailureReason() != null ? pointLog.getFailureReason().name() : null)
                .routineId(pointLog.getRoutineId())
                .purchaseId(pointLog.getPurchaseId())
                .createdAt(pointLog.getCreatedAt())
                .commitDate(pointLog.getCommitDate())
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
