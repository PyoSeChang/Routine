// src/components/ui/TagInput.tsx

import React, { useState } from "react";

interface Props {
    tags: string[];
    setTags: (tags: string[]) => void;
}

export default function TagInput({ tags, setTags }: Props) {
    const [inputValue, setInputValue] = useState("");

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if ((e.key === "Enter" || e.key === ",") && inputValue.trim()) {
            e.preventDefault();
            const newTag = inputValue.trim();
            if (tags.length >= 10) return;
            if (!tags.includes(newTag)) {
                setTags([...tags, newTag]);
            }
            setInputValue("");
        }
    };

    const handleRemoveTag = (tagToRemove: string) => {
        setTags(tags.filter(tag => tag !== tagToRemove));
    };

    return (
        <div className="mb-4">
            <label className="block font-medium mb-1">태그</label>
            <input
                type="text"
                placeholder="쉼표(,) 또는 Enter로 태그 추가 (최대 10개)"
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyDown={handleKeyDown}
                className="border p-2 rounded w-full"
            />
            <div className="flex flex-wrap gap-2 mt-2">
                {tags.map((tag, index) => (
                    <div
                        key={index}
                        className="bg-gray-200 text-sm px-3 py-1 rounded-full flex items-center gap-1"
                    >
                        #{tag}
                        <button
                            onClick={() => handleRemoveTag(tag)}
                            className="text-gray-600 hover:text-red-500 ml-1"
                        >
                            ×
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}
