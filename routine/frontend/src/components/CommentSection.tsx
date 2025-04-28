// src/components/CommentSection.tsx
import React, { useState } from 'react';
import { CommentForm } from './CommentForm';
import { CommentItem } from './CommentItem';
import { EditCommentModal } from './EditCommentModal';
import { useComments } from '../hooks/useComment';
import { CommentDTO } from '../types/comment';

interface CommentSectionProps {
    boardId: string;
}

export function CommentSection({ boardId }: CommentSectionProps) {
    const { comments, loading, error, add, update, remove } = useComments(boardId);
    const [replyTo, setReplyTo] = useState<number | null>(null);
    const [editing, setEditing] = useState<CommentDTO | null>(null);

    const handleAdd = async (content: string, parentId?: number | null) => {
        await add(content, parentId ?? null);
        setReplyTo(null);
    };


    if (loading) {
        return (
            <div className="mt-6">
                <h3 className="text-lg font-semibold mb-2">댓글</h3>
                <CommentForm parentId={null} onSubmit={handleAdd} onCancelReply={() => setReplyTo(null)} />
                <p className="p-4">댓글 로딩중…</p>
            </div>
        );
    }

    return (
        <div className="mt-6">
            <h3 className="text-lg font-semibold mb-2">댓글</h3>

            {/* 1) 댓글 입력폼: 항상 노출 */}
            <CommentForm
                parentId={replyTo}
                onSubmit={handleAdd}
                onCancelReply={() => setReplyTo(null)}
            />

            {/* 2) 로딩 이후 에러 처리 */}
            {error && <p className="text-red-500 mb-4">{error}</p>}

            {/* 3) 댓글 리스트 */}
            {comments.length === 0 && !error ? (
                <p className="p-4 text-gray-500">등록된 댓글이 없습니다.</p>
            ) : (
                comments.map(comment => (
                    <CommentItem
                        key={comment.commentId}
                        comment={comment}
                        onReply={id => setReplyTo(id)}
                        onEdit={c => setEditing(c)}
                        onDelete={id => remove(id)}
                    />
                ))
            )}

            {/* 4) 수정 모달 */}
            {editing && (
                <EditCommentModal
                    comment={editing}
                    onSave={async (id, content) => {
                        await update(id, content);
                        setEditing(null);
                    }}
                    onCancel={() => setEditing(null)}
                />
            )}
        </div>
    );
}