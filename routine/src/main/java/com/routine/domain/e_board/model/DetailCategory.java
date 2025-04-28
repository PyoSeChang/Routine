package com.routine.domain.e_board.model;

import lombok.Getter;

// DetailCategory.java
@Getter
public enum DetailCategory {
    // 외국어
    ENGLISH("영어", Category.LANGUAGE),
    JAPANESE("일본어", Category.LANGUAGE),
    CHINESE("중국어", Category.LANGUAGE),

    // 취업
    IT("IT", Category.EMPLOYMENT),
    FINANCE("금융", Category.EMPLOYMENT),
    MANAGEMENT_OFFICE("경영/사무", Category.EMPLOYMENT),
    MARKETING_ADVERTISEMENT("마케팅/광고", Category.EMPLOYMENT),
    DESIGN("디자인", Category.EMPLOYMENT),
    CONSTRUCTION("건설/토목", Category.EMPLOYMENT),
    MANUFACTURING("생산/제조", Category.EMPLOYMENT),
    LOGISTICS("유통/물류", Category.EMPLOYMENT),
    EDUCATION("교육", Category.EMPLOYMENT),
    MEDICAL_PHARMACY_WELFARE("의료/제약/복지", Category.EMPLOYMENT),
    LEGAL("법률/법무", Category.EMPLOYMENT),
    MEDIA_CULTURE_ART("미디어/문화/예술", Category.EMPLOYMENT),
    SERVICE_SALES("서비스/영업", Category.EMPLOYMENT),
    PUBLIC_AGENCY("공공기관/공기업", Category.EMPLOYMENT),

    // 학습
    MAJOR("전공 공부", Category.STUDY),
    CERTIFICATE("자격증 준비", Category.STUDY),

    // 라이프
    HEALTH("건강", Category.LIFE),
    EXERCISE("운동", Category.LIFE),
    DAILY("데일리", Category.LIFE);

    private final String displayName;
    private final Category parentCategory;

    DetailCategory(String displayName, Category parentCategory) {
        this.displayName = displayName;
        this.parentCategory = parentCategory;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Category getParentCategory() {
        return parentCategory;
    }
}

