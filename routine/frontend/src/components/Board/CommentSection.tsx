import React, { useState } from 'react';
import { useComments } from '../../hooks/useComment';
import { CommentDTO } from '../../types/comment';
import CommentPostItList from './CommentPostItList';
import CommentPostItItem from './CommentPostItItem';

interface CommentSectionProps {
    boardId: number;
    initialComments: CommentDTO[];
}

export function CommentSection({ boardId, initialComments }: CommentSectionProps) {
    const { comments, loading, error, add, update, remove } = useComments(boardId, initialComments);
    const [replyTo, setReplyTo] = useState<number | null>(null);
    const [editingId, setEditingId] = useState<number | null>(null);

    const handleAdd = async (content: string, parentId?: number | null) => {
        await add(content, parentId ?? null);
        setReplyTo(null);
    };

    const handleUpdate = async (id: number, content: string) => {
        await update(id, content);
        setEditingId(null);
    };

    return (
        <div className="mt-6">
            <h3 className="text-lg font-semibold mb-4">댓글</h3>

            {/* 입력 폼 (기본) */}
            <CommentPostItItem
                comment={{
                    commentId: 0,
                    writerId: 0,
                    writerName: '',
                    content: '',
                    parentId: replyTo,
                    boardId,
                    createdAt: new Date().toISOString(),
                    updatedAt: undefined,
                }}
                mode="input"
                onSave={(content) => handleAdd(content, replyTo)}
                onCancel={() => setReplyTo(null)}
            />

            {/* 에러 메시지 */}
            {error && <p className="text-red-500 my-2">{error}</p>}
            <h3 className="text-lg font-semibold font-ui mt-4 mb-4">댓글 목록</h3>
            {/* 댓글 목록 */}
            {loading ? (
                <p className="p-4">댓글 로딩중…</p>
            ) : (comments?.length ?? 0) === 0 ? (
                <p className="p-4 text-gray-500">등록된 댓글이 없습니다.</p>
            ) : (
                <CommentPostItList
                    comments={comments}
                    editingId={editingId}
                    onReply={setReplyTo}
                    onEdit={(comment) => setEditingId(comment.commentId)}
                    onDelete={remove}
                    onSaveEdit={handleUpdate}
                    onCancelEdit={() => setEditingId(null)}
                />
            )}
        </div>
    );
}
