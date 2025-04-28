// src/pages/BoardDetailPage.tsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from '../api/axios';
import BoardNav from '../components/Board/BoardNav';
import { BoardDTO } from '../types/board';
import { format } from 'date-fns';
import { useCurrentUser } from '../hooks/useAuth';
import { CommentSection } from '../components/CommentSection';

export default function BoardDetailPage() {
    const { boardId } = useParams<{ boardId: string }>();
    const navigate = useNavigate();

    const [board, setBoard] = useState<BoardDTO | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const currentUser = useCurrentUser();
    const canModify = !!board && !!currentUser && currentUser.memberId === board.writer;

    useEffect(() => {
        if (!boardId) return;
        (async () => {
            setLoading(true);
            try {
                const res = await axios.get<BoardDTO>(`/boards/${boardId}`);
                setBoard(res.data);
            } catch (e: any) {
                console.error(e);
                setError(e.response?.data?.message || e.message);
            } finally {
                setLoading(false);
            }
        })();
    }, [boardId]);

    const handleDelete = async () => {
        if (!boardId) return;
        if (!window.confirm('정말 이 글을 삭제하시겠습니까?')) return;

        try {
            await axios.delete(`/boards/${boardId}`);
            alert('삭제되었습니다.');
            navigate('/boards');
        } catch (e: any) {
            console.error('삭제 실패', e);
            alert('삭제 중 오류가 발생했습니다.');
        }
    };

    if (loading) return <p className="p-4">로딩 중…</p>;
    if (error) return <p className="p-4 text-red-500">에러: {error}</p>;
    if (!board) return <p className="p-4">게시글을 찾을 수 없습니다.</p>;

    const displayDate = board.updatedAt
        ? { label: '수정일', date: board.updatedAt }
        : { label: '작성일', date: board.createdAt! };

    return (
        <div className="flex">
            <BoardNav />
            <div className="flex-1 p-6 bg-white">
                {canModify && (
                    <div className="flex justify-end space-x-2 mb-4">
                        <button
                            onClick={() => navigate(`/boards/edit/${boardId}`)}
                            className="px-4 py-2 bg-yellow-400 text-white rounded hover:bg-yellow-500"
                        >수정</button>
                        <button
                            onClick={handleDelete}
                            className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                        >삭제</button>
                    </div>
                )}

                <h1 className="text-2xl font-bold mb-4">{board.title}</h1>
                <div className="text-sm text-gray-600 mb-2">
                    작성자: {board.writer}
                    {'  |  '}
                    {displayDate.label}: {format(new Date(displayDate.date), 'yyyy-MM-dd HH:mm')}
                    {'  |  '}
                    조회수: {board.viewCount ?? '-'}
                </div>
                <div className="mb-4">
                    <span className="inline-block bg-gray-200 px-2 py-1 text-xs rounded">
                        {board.category} / {board.detailCategory}
                    </span>
                    <span className="inline-block bg-blue-100 px-2 py-1 text-xs rounded ml-2">
                        {board.boardType}
                    </span>
                </div>
                <article className="prose max-w-none">
                    {board.content}
                </article>
                {board.tags && (
                    <div className="mt-6">
                        <h4 className="font-semibold mb-1">태그</h4>
                        <div className="flex flex-wrap gap-2">
                            {board.tags.split(',').map(tag => (
                                <span key={tag} className="bg-green-100 px-2 py-1 rounded text-xs">
                                    #{tag.trim()}
                                </span>
                            ))}
                        </div>
                    </div>
                )}

                {/* 댓글 섹션 */}
                <CommentSection boardId={boardId!} />
            </div>
        </div>
    );
}