import React, { useRef } from 'react';
import Line from './Line';

interface TextareaOnNoteProps {
    label?: string;
    value: string;
    onChange: (value: string) => void;
    placeholder?: string;
    maxRows?: number;
}

const TextareaOnNote: React.FC<TextareaOnNoteProps> = ({
                                                           label = '',
                                                           value,
                                                           onChange,
                                                           placeholder = '',
                                                           maxRows = 4,
                                                       }) => {
    const lines = value.split('\n');
    const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

    // 최소 한 줄은 보장
    while (lines.length < 2) lines.push('');

    const handleChange = (i: number, newValue: string) => {
        const updated = [...lines];
        updated[i] = newValue;
        onChange(updated.join('\n'));
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>, i: number) => {
        const isEmpty = lines[i] === '';

        // ⬅ Backspace → 위로 커서 이동
        if (e.key === 'Backspace' && isEmpty && i > 0) {
            e.preventDefault();
            inputRefs.current[i - 1]?.focus();
            return;
        }

        // ↩ Enter → 아래로 커서 이동 or 줄 추가
        if (e.key === 'Enter') {
            e.preventDefault();

            const isAtMax = lines.length >= maxRows;

            if (!isAtMax) {
                // ✅ 줄 추가 + 커서 이동
                const updated = [...lines];
                updated.splice(i + 1, 0, '');
                onChange(updated.join('\n'));

                requestAnimationFrame(() => {
                    inputRefs.current[i + 1]?.focus();
                });
            } else if (i < maxRows - 1) {
                // ✅ 줄 추가 없이 아래줄로 커서 이동
                requestAnimationFrame(() => {
                    inputRefs.current[i + 1]?.focus();
                });
            }
        }
    };

    return (
        <div className="w-full">
            {lines.map((line, i) => (
                <Line key={i} className="gap-2 px-2">
                    {i === 0 && label && (
                        <span className="shrink-0 font-ui text-base whitespace-nowrap">{label}</span>
                    )}
                    <input
                        ref={(el) => {
                            inputRefs.current[i] = el;
                        }}
                        type="text"
                        value={line}
                        placeholder={i === 0 ? placeholder : ''}
                        onChange={(e) => handleChange(i, e.target.value)}
                        onKeyDown={(e) => handleKeyDown(e, i)}
                        className="w-full bg-note font-user text-base py-0 px-2 h-[36px] focus:outline-none"
                    />
                </Line>
            ))}
        </div>
    );
};

export default TextareaOnNote;
