// src/components/CommentItem.tsx
import React from 'react';
import { CommentDTO } from '../../types/comment';
import { useCurrentUser } from '../../hooks/useAuth';

interface CommentItemProps {
    comment: CommentDTO;
    level?: number;
    onReply: (id: number) => void;
    onEdit: (comment: CommentDTO) => void;
    onDelete: (id: number) => void;
}

export function CommentItem({ comment, level = 0, onReply, onEdit, onDelete }: CommentItemProps) {
    const user = useCurrentUser();
    const canModify = user && user.memberId === comment.writerId;

    const handleClick = () => onReply(comment.commentId);
    console.log('comment.colorId', comment.colorId);
    const postItColors = [
        '#fff9c4', '#ffe0b2', '#f8bbd0',
        '#c8e6c9', '#b3e5fc', '#d1c4e9', '#e0f7fa'
    ];

    const bgColor = postItColors[comment.colorId ?? 0];

    return (
        <div
            onClick={handleClick}
            className={`p-2 mb-2 rounded ${level > 0 ? 'ml-8' : ''}`}
            style={{ backgroundColor: bgColor }}
        >
            <div className="flex justify-between items-start">
                <div>
                    <span className="font-semibold">{comment.writerName}</span>
                    <span className="ml-2 text-xs text-gray-500">{comment.createdAt}</span>
                </div>
                {canModify && (
                    <div className="space-x-2 text-xs">
                        <button
                            onClick={e => { e.stopPropagation(); onEdit(comment); }}
                            className="text-yellow-500"
                        >수정</button>
                        <button
                            onClick={e => { e.stopPropagation(); onDelete(comment.commentId); }}
                            className="text-red-500"
                        >삭제</button>
                    </div>
                )}
            </div>
            <p className="mt-1 whitespace-pre-wrap">{comment.content}</p>

            {/* 자식 댓글 */}
            {comment.replies?.map(reply => (
                <CommentItem
                    key={reply.commentId}
                    comment={reply}
                    level={level + 1}
                    onReply={onReply}
                    onEdit={onEdit}
                    onDelete={onDelete}
                />
            ))}
        </div>
    );
}
