// src/hooks/useComments.ts
import { useState, useEffect, useCallback } from 'react';
import axios from '../api/axios';
import { CommentDTO } from '../types/comment';

interface UseCommentsResult {
    comments: CommentDTO[];
    loading: boolean;
    error: string | null;
    reload: () => void;
    add: (content: string, parentId?: number | null) => Promise<void>;
    update: (commentId: number, content: string) => Promise<void>;
    remove: (commentId: number) => Promise<void>;
}

export function useComments(boardId: string): UseCommentsResult {
    const [comments, setComments] = useState<CommentDTO[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetch = useCallback(async () => {
        setLoading(true);
        try {
            const res = await axios.get<CommentDTO[]>(`/boards/${boardId}/comments`);
            setComments(res.data);
            setError(null);
        } catch (e: any) {
            setError(e.message || '댓글을 불러오는 중 오류 발생');
        } finally {
            setLoading(false);
        }
    }, [boardId]);

    useEffect(() => { fetch(); }, [fetch]);

    const add = async (content: string, parentId: number | null = null) => {
        await axios.post<CommentDTO>(`/boards/${boardId}/comments`, { content, parentId });
        await fetch();
    };
    const update = async (commentId: number, content: string) => {
        await axios.put<CommentDTO>(`/boards/${boardId}/comments/${commentId}`, { content });
        await fetch();
    };
    const remove = async (commentId: number) => {
        await axios.delete(`/boards/${boardId}/comments/${commentId}`);
        await fetch();
    };

    return { comments, loading, error, reload: fetch, add, update, remove };
}