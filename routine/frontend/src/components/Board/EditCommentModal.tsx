// src/components/EditCommentModal.tsx
import React, { useState } from 'react';
import { CommentDTO } from '../../types/comment';

interface EditCommentModalProps {
    comment: CommentDTO;
    onSave: (id: number, content: string) => void;
    onCancel: () => void;
}

export function EditCommentModal({ comment, onSave, onCancel }: EditCommentModalProps) {
    const [value, setValue] = useState(comment.content);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSave(comment.commentId, value);
    };

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
            <form onSubmit={handleSubmit} className="bg-white p-4 rounded w-96">
                <h2 className="mb-2 font-bold">댓글 수정</h2>
                <textarea
                    value={value}
                    onChange={e => setValue(e.target.value)}
                    className="w-full border p-2 rounded h-32"
                />
                <div className="flex justify-end mt-2 space-x-2">
                    <button type="button" onClick={onCancel} className="px-3 py-1">취소</button>
                    <button type="submit" className="px-3 py-1 bg-green-500 text-white rounded">저장</button>
                </div>
            </form>
        </div>
    );
}