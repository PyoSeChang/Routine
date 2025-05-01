// src/types/board.ts

export enum Category {
    NONE = 'NONE',
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
    writerNickname: string;
    title: string;
    content: string;
    tags?: string;
    category: Category;
    detailCategory: string;
    boardType: BoardType;
    createdAt?: string;
    updatedAt?: string;
    viewCount?: number;
    comments?: CommentDTO[];
}

export interface CommentDTO {
    commentId: number;
    boardId: number;
    writerId: number;
    writerName: string;
    content: string;
    parentId: number | null;
    createdAt: string;
    updatedAt?: string;
}


// 목록 조회 DTO
export interface BoardListDTO {
    boardId: number;
    writerId: number;
    writerName: string;
    title: string;
    category: string;       // displayName
    detailCategory: string; // displayName
    tags?: string;
    modifiedDate: string;   // 이미 포맷된 문자열
    viewCount: number;
    boardType: BoardType;
}

// 페이징 응답 타입
export interface PageResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    // 필요 시 아래 메타데이터 추가 가능
    // page?: number;
    // size?: number;
    // sort?: any;
}
