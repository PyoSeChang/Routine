import React, { useState } from 'react';
import Line from './Line';
import TagOnNote from './TagOnNote';

interface TagInputOnNoteProps {
    tags: string[];
    setTags: (tags: string[]) => void;
}

const TagInputOnNote: React.FC<TagInputOnNoteProps> = ({ tags, setTags }) => {
    const [inputValue, setInputValue] = useState('');

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if ((e.key === 'Enter' || e.key === ',') && inputValue.trim()) {
            e.preventDefault();
            const newTag = inputValue.trim();
            if (tags.length >= 10) return;
            if (!tags.includes(newTag)) {
                setTags([...tags, newTag]);
            }
            setInputValue('');
        }
    };

    const handleRemoveTag = (tagToRemove: string) => {
        setTags(tags.filter(tag => tag !== tagToRemove));
    };

    return (
        <div className="w-full">
            <Line>
                <span className="shrink-0 font-ui text-base whitespace-pre">태그: </span>
                <input
                    type="text"
                    value={inputValue}
                    placeholder="쉼표(,) 또는 Enter로 태그 추가 (최대 10개)"
                    onChange={(e) => setInputValue(e.target.value)}
                    onKeyDown={handleKeyDown}
                    className="w-full h-full bg-note font-user text-base py-2 focus:outline-none"
                />
            </Line>
            <Line>
            <div className="flex flex-wrap gap-2 mt-1 mb-1 px-1">
                {tags.map((tag) => (
                    <TagOnNote
                        key={tag}
                        tag={tag}
                        onRemove={() => handleRemoveTag(tag)}
                    />
                ))}
            </div>
            </Line>
        </div>
    );
};

export default TagInputOnNote;
