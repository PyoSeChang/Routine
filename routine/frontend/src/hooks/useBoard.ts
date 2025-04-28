// src/types/board.ts
// (기존 DTO 타입은 그대로 두고, 페이징 응답용 타입만 추가)
export enum Category {
    LANGUAGE = 'LANGUAGE',
    EMPLOYMENT = 'EMPLOYMENT',
    STUDY = 'STUDY',
    LIFE = 'LIFE',
}

export enum BoardType {
    NOTICE = 'NOTICE',
    PROMOTION = 'PROMOTION',
    REVIEW = 'REVIEW',
    QNA = 'QNA',
    INFORMATION = 'INFORMATION',
}

// 상세 조회 DTO
export interface BoardDTO {
    boardId?: number;
    writer: number;
    title: string;
    content: string;
    tags?: string;
    category: Category;
    detailCategory: string;
    boardType: BoardType;
    createdAt?: string;
    updatedAt?: string;
    viewCount?: number;
}

// 목록 조회 DTO
export interface BoardListDTO {
    boardId: number;
    writerId: number;
    writerName: string;
    title: string;
    category: string;
    detailCategory: string;
    tags?: string;
    modifiedDate: string;
    viewCount: number;
    boardType: BoardType;
}

// 페이징 응답 공용 타입
export interface PageResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;

}
