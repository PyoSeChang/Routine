// components/ui/DropdownOnNote.tsx
import React, { useState, useRef, useEffect } from 'react';
import Line from './Line';

interface Option {
    label: string;
    value: string;
}

interface DropdownOnNoteProps {
    value: string;
    options: Option[];
    onChange: (value: string) => void;
    placeholder?: string;
    label?: string;
}

const DropdownOnNote: React.FC<DropdownOnNoteProps> = ({ value, options, onChange, placeholder = '선택하세요' }) => {
    const [open, setOpen] = useState(false);
    const ref = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const handleClickOutside = (e: MouseEvent) => {
            if (ref.current && !ref.current.contains(e.target as Node)) {
                setOpen(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    return (
        <Line className="relative" ref={ref}>
            <span className="shrink-0 font-ui text-base whitespace-pre">게시글 유형: </span>
            <button
                onClick={() => setOpen((prev) => !prev)}
                className="w-full flex justify-between items-center text-left font-ui text-base focus:outline-none"
            >
                <span>{options.find(opt => opt.value === value)?.label || placeholder}</span>
                <span className="text-sm text-gray-500">▾</span>
            </button>
            {open && (
                <ul className="absolute top-full left-0 w-full bg-note z-10 shadow border border-blue-200">
                    {options.map((opt) => (
                        <Line
                            key={opt.value}
                            className={`font-ui cursor-pointer hover:bg-blue-100 ${
                                opt.value === value ? 'bg-blue-50 font-semibold' : ''
                            }`}
                            onClick={() => {
                                onChange(opt.value);
                                setOpen(false);
                            }}
                        >
                            {opt.label}
                        </Line>
                    ))}
                </ul>
            )}
        </Line>
    );
};

export default DropdownOnNote;