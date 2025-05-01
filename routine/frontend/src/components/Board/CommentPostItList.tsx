import React from 'react';
import { CommentDTO } from '../../types/board';
import CommentPostItItem from './CommentPostItItem';

interface Props {
    comments: CommentDTO[];
    currentUserId?: number;
    onReply?: (commentId: number) => void;
    onEdit?: (comment: CommentDTO) => void;
    onDelete?: (commentId: number) => void;
    editingId?: number | null;
    onSaveEdit?: (id: number, content: string) => void;
    onCancelEdit?: () => void;
}

const CommentPostItList: React.FC<Props> = ({
                                                comments,
                                                onReply,
                                                onEdit,
                                                onDelete,
                                                editingId,
                                                onSaveEdit,
                                                onCancelEdit,
                                            }) => {
    return (
        <div className="space-y-4">
            {comments.map(comment => (
                <CommentPostItItem
                    key={comment.commentId}
                    comment={comment}
                    mode={editingId === comment.commentId ? 'edit' : 'view'}
                    onReply={onReply}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onSave={(content) => onSaveEdit?.(comment.commentId, content)}
                    onCancel={onCancelEdit}
                />
            ))}
        </div>
    );
};

export default CommentPostItList;
