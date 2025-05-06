import React, { useEffect, useLayoutEffect, useRef, useState } from 'react';
import PostItSVG from '../ui/post_it/PostItSVG';
import { CommentDTO } from '../../types/comment';
import { useCurrentUser } from '../../hooks/useAuth';

interface Props {
    comment: CommentDTO;
    mode?: 'view' | 'edit' | 'input';
    onReply?: (commentId: number) => void;
    onEdit?: (comment: CommentDTO) => void;
    onDelete?: (commentId: number) => void;
    onSave?: (content: string) => void;
    onCancel?: () => void;
}

const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
    });
};

const CommentPostItItem: React.FC<Props> = ({
                                                comment,
                                                mode = 'view',
                                                onReply,
                                                onEdit,
                                                onDelete,
                                                onSave,
                                                onCancel,
                                            }) => {
    const { memberId: currentUserId } = useCurrentUser() ?? {};
    const [content, setContent] = useState(comment.content || '');
    const [height, setHeight] = useState(100);
    const ref = useRef<HTMLDivElement>(null);
    const textareaRef = useRef<HTMLTextAreaElement>(null);

    useLayoutEffect(() => {
        if (ref.current) {
            const measured = ref.current.offsetHeight;
            setHeight(measured + 40); // padding + margin 포함 여유
        }
    }, [content]);

    useEffect(() => {
        if (mode === 'edit' && textareaRef.current) {
            textareaRef.current.focus();
        }
    }, [mode]);

    return (
        <div className={comment.parentId ? 'ml-8' : ''}>
            <PostItSVG width={300} height={height} variant={comment.colorId}>
                <div ref={ref} className="text-sm leading-snug break-words w-full">
                    {mode === 'view' && (
                        <>
                            <div className="flex justify-between items-start">
                                <div>
                                    <span className="font-bold">{comment.writerName}</span>
                                    <span className="ml-2 text-xs text-gray-500">{formatDate(comment.createdAt)}</span>
                                </div>
                                {comment.writerId === currentUserId && (
                                    <div className="space-x-2 text-xs">
                                        <button onClick={() => onEdit?.(comment)} className="text-yellow-500">수정</button>
                                        <button onClick={() => onDelete?.(comment.commentId)} className="text-red-500">삭제</button>
                                    </div>
                                )}
                            </div>
                            <p className="font-user text-[16pt] mt-2 whitespace-pre-wrap">{comment.content}</p>
                            {onReply && (
                                <button
                                    onClick={() => onReply(comment.commentId)}
                                    className="mt-2 text-xs text-blue-500"
                                >
                                    답글 달기
                                </button>
                            )}
                        </>
                    )}

                    {(mode === 'input' || mode === 'edit') && (
                        <div>
              <textarea
                  ref={textareaRef}
                  className={`w-full font-user text-[17pt] resize-none p-1 text-sm rounded bg-[#fff9c4] placeholder-gray-400 border-none focus:outline-none focus:ring-0 focus:border-none`}
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                  rows={3}
                  placeholder="댓글 내용을 작성해주세요"
              />
                            <div className="flex justify-end gap-2 mt-2 text-xs">
                                <button
                                    className="text-blue-500"
                                    onClick={() => onSave?.(content)}
                                >
                                    저장
                                </button>
                                <button
                                    className="text-gray-400"
                                    onClick={onCancel}
                                >
                                    취소
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </PostItSVG>
        </div>
    );
};

export default CommentPostItItem;
