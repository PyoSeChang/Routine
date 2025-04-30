package com.routine.domain.b_circle.dto;


import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.b_circle.model.CircleMember;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CircleSummaryDTO {
    private Long circleId;                   // 식별자 (클릭용)
    private String name;               // 서클 이름
    private String category;                 // 대분류
    private String detailCategory;           // 소분류
    private String description;
    private int currentMemberCount;          // 현재 인원
    private int maxMemberCount;
    private boolean isLeader;
    private LocalDateTime createdAt;
    private LocalDateTime joinedAt;
    private List<String> tags;



    //  검색 결과용
    public static CircleSummaryDTO from(Circle circle, int currentMemberCount) {
        CircleSummaryDTO dto = new CircleSummaryDTO();
        dto.circleId = circle.getId();
        dto.name = circle.getName();
        dto.category = circle.getCategory().getDisplayName();
        dto.detailCategory = circle.getDetailCategory().getDisplayName();
        dto.description = circle.getDescription();
        dto.maxMemberCount = circle.getMaxMemberCount();
        dto.currentMemberCount = currentMemberCount;
        dto.createdAt = circle.getCreatedAt();
        dto.tags = parseTags(circle.getTags());
        // isLeader=false, joinedAt=null
        return dto;
    }

    //  내 서클 목록용
    public static CircleSummaryDTO from(Circle circle, CircleMember cm, int currentMemberCount) {
        CircleSummaryDTO dto = from(circle, currentMemberCount);
        dto.isLeader = (cm.getRole() == CircleMember.Role.LEADER);
        dto.joinedAt = cm.getJoinedAt();
        return dto;
    }

    //  태그 파싱용 메서드
    private static List<String> parseTags(String tagString) {
        if (tagString == null || tagString.isBlank()) return List.of();
        return List.of(tagString.split(","))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

}
