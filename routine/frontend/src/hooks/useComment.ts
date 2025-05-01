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

// useComments.ts
export function useComments(boardId: number, initial: CommentDTO[] = []) {
    const [comments, setComments] = useState<CommentDTO[]>(initial);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // ✅ 초기 댓글이 나중에라도 들어올 때 반영
    useEffect(() => {
        setComments(initial);
    }, [initial]);

    const add = async (content: string, parentId: number | null) => {
        const res = await axios.post(`/boards/${boardId}/comments`, { boardId, content, parentId });
        setComments(prev => [...prev, res.data]);
    };

    const update = async (id: number, content: string) => {
        await axios.put(`/boards/${boardId}/comments/${id}`, { content });
        setComments(prev => prev.map(c => (c.commentId === id ? { ...c, content } : c)));
    };

    const remove = async (id: number) => {
        await axios.delete(`/boards/${boardId}/comments/${id}`);
        setComments(prev => prev.filter(c => c.commentId !== id));
    };

    return { comments, loading, error, add, update, remove };
}

