// src/pages/BoardDetailPage.tsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from '../../api/axios';
import BoardNav from '../../components/Board/BoardNav';
import { BoardDTO } from '../../types/board';
import { format } from 'date-fns';
import { useCurrentUser } from '../../hooks/useAuth';
import BoardDetailOnNote  from '../../components/Board/BoardDetailOnNote';
import { CommentSection } from '../../components/Board/CommentSection';
import CommentPostItList from "../../components/Board/CommentPostItList";
import PostItBoardNav from "../../components/Board/PostItBoardNav";
import AppLayout from "../../layout/AppLayout";


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
        <AppLayout>
            {/* 네비게이션 (왼쪽 고정) */}
            <div className="shrink-0 mr-6">
                <PostItBoardNav />
            </div>

            {/* 본문 콘텐츠 (중앙 정렬, 폭 제한) */}
            <div className="flex-1 w-full ">
                <BoardDetailOnNote
                    board={board}
                    displayDate={displayDate}
                    canModify={canModify}
                    onEdit={() => navigate(`/boards/edit/${boardId}`)}
                    onDelete={handleDelete}
                />

                {/* 댓글 */}
                <CommentSection boardId={board.boardId!} initialComments={board.comments ?? []} />
            </div>
        </AppLayout>




    );
}