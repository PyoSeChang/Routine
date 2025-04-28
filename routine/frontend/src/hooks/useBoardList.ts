// src/hooks/useBoardList.ts
import { useState, useEffect } from 'react';
import axios from '../api/axios';
import { BoardListDTO, PageResponse } from '../types/board';

interface UseBoardListResult {
    boards: BoardListDTO[];
    totalPages: number;
    loading: boolean;
}

export function useBoardList(
    category?: string,
    detailCategory?: string,
    keyword?: string,
    page: number = 0,
    size: number = 10
): UseBoardListResult {
    const [boards, setBoards] = useState<BoardListDTO[]>([]);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const params: Record<string, string | number> = { page, size };
        if (category)       params.category = category;
        if (detailCategory) params.detailCategory = detailCategory;
        if (keyword)        params.keyword = keyword;

        const fetchData = async () => {
            setLoading(true);
            try {
                const res = await axios.get<PageResponse<BoardListDTO>>(
                    '/boards',
                    { params }
                );
                setBoards(res.data.content);
                setTotalPages(res.data.totalPages);
            } catch (error) {
                console.error('useBoardList error', error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [category, detailCategory, keyword, page, size]);

    return { boards, totalPages, loading };
}
