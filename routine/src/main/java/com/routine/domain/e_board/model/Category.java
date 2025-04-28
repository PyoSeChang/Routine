package com.routine.domain.e_board.model;

public enum Category {
    LANGUAGE("외국어"),
    EMPLOYMENT("취업"),
    STUDY("학습"),
    LIFE("라이프");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
