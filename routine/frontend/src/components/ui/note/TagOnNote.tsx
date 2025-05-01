import React from 'react';

interface TagOnNoteProps {
    tag: string;
    onRemove?: () => void;
}

const TagOnNote: React.FC<TagOnNoteProps> = ({ tag, onRemove }) => {
    return (
        <div className="bg-lime-300/60 text-sm font-user px-3 py-1 rounded-full flex items-center gap-1">
            <span>#{tag}</span>
            {onRemove && (
                <button
                    onClick={onRemove}
                    className="text-gray-600 hover:text-red-500"
                >
                    ×
                </button>
            )}
        </div>
    );
};

export default TagOnNote;
