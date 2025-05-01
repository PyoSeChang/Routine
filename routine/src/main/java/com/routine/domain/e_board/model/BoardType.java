package com.routine.domain.e_board.model;

public enum BoardType {
    NOTICE("공지"),
    PROMOTION("홍보"),
    REVIEW("후기"),
    QNA("질문"),
    INFORMATION("정보");

    private final String displayName;

    BoardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
