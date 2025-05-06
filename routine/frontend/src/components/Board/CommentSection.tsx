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
        <div className="flex gap-10 mb-40">
            {/* 댓글 입력 + 목록 */}
            <div className="flex-1 max-w-[400px]">
                <h3 className="font-chalk text-white text-[42px] mb-4">Post Comment</h3>

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

                {error && <p className="text-red-500 my-2">{error}</p>}

                <h3 className="text-[42px] font-chalk text-white mt-4 mb-4">Comments</h3>

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

            {/* 오른쪽 이모티콘 컬럼 */}
            <div className="w-[120px] flex flex-col items-center gap-40 pt-[68px] mt-28">
                <Emoticon src="/assets/img/no-doodle.png" alt="낙서금지 루틴이" />
                <Emoticon src="/assets/img/noisy-student.png" alt="떠든사람 루틴이" />
                <Emoticon src="/assets/img/science-time.png" alt="과학시간 루틴이" />
            </div>
        </div>
    );
}

// 이모티콘 컴포넌트
const Emoticon: React.FC<{ src: string; alt: string }> = ({ src, alt }) => (
    <div className="w-fit">
        <img
            src={src}
            alt={alt}
            className="mx-auto"
            style={{
                width: '220px',
                transform: 'scale(2.2)',
                transformOrigin: 'bottom left',
            }}
        />
    </div>
);
