// NoteBlock.tsx
import React from 'react';

interface NoteBlockProps {
    className?: string;
    children: React.ReactNode;
}

const NoteBlock: React.FC<NoteBlockProps> = ({ className = '', children }) => {
    return (
        <div className={`relative bg-note pr-2 py-4 flex ${className}`}>
            <div
                className="absolute top-0 left-[2.5rem] w-[2px] h-full"
                style={{ backgroundColor: '#f28b82' }}
            />
            {children}
        </div>
    );
};

export default NoteBlock;
