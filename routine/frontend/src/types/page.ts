// src/types/page.ts
export interface PageResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
}