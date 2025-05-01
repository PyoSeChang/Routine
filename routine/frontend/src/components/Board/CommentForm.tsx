// src/components/CommentForm.tsx
import React, { useState, useEffect, useRef } from 'react';

interface CommentFormProps {
    /**
     * 답글 작성 시 참조할 부모 댓글 ID. 루트 댓글인 경우 undefined
     */
    parentId?: number | null;
    /**
     * 댓글 등록 또는 답글 등록
     * @param content 댓글 내용
     * @param parentId (선택) 부모 댓글 ID
     */
    onSubmit: (content: string, parentId?: number | null) => void | Promise<void>;
    /** 답글 모드 취소 버튼 클릭 시 호출 */
    onCancelReply: () => void;
}

export const CommentForm: React.FC<CommentFormProps> = ({ parentId, onSubmit, onCancelReply }) => {
    const [content, setContent] = useState('');
    const textareaRef = useRef<HTMLTextAreaElement>(null);

    // reply mode 에 진입했을 때 textarea 포커스
    useEffect(() => {
        if (parentId != null && textareaRef.current) {
            textareaRef.current.focus();
        }
    }, [parentId]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!content.trim()) return;
        await onSubmit(content.trim(), parentId);
        setContent('');
    };

    return (
        <form onSubmit={handleSubmit} className="mb-4">
            {parentId != null && (
                <div className="text-sm text-gray-500 mb-1">
                    답글 쓰기 <button type="button" onClick={onCancelReply} className="underline">취소</button>
                </div>
            )}
            <textarea
                ref={textareaRef}
                value={content}
                onChange={e => setContent(e.target.value)}
                placeholder="댓글을 입력하세요..."
                className="w-full border rounded p-2 mb-2"
                rows={3}
            />
            <button type="submit" className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
                {parentId != null ? '답글 등록' : '댓글 등록'}
            </button>
        </form>
    );
};
