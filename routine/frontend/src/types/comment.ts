// src/types/comment.ts
export interface CommentDTO {
    commentId: number;
    boardId: number;
    writerId: number;
    writerName: string;
    content: string;
    parentId: number | null;
    createdAt: string;
    updatedAt?: string;
    replies?: CommentDTO[];
}