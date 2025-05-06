import React, { useRef } from 'react';
import Line from './Line';

interface TextareaOnNoteProps {
    label?: string;
    value: string;
    onChange: (value: string) => void;
    placeholder?: string;
    maxRows?: number;
    minRows?: number;
    maxCharsPerLine?: number;
}

const TextareaOnNote: React.FC<TextareaOnNoteProps> = ({
                                                           label = '',
                                                           value,
                                                           onChange,
                                                           placeholder = '',
                                                           maxRows = 4,
                                                           minRows = 1,
                                                           maxCharsPerLine = 100,
                                                       }) => {
    const lines = value.split('\n');
    const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

    // 최소 줄 수 보장
    while (lines.length < minRows) lines.push('');

    const handleChange = (i: number, newValue: string) => {
        const splitLines = newValue.split('\n');

        const updated = [...lines];
        updated.splice(i, 1, ...splitLines);

        // 최대 줄 수 제한
        if (updated.length > maxRows) {
            updated.length = maxRows;
        }

        onChange(updated.join('\n'));

        // 커서 이동
        requestAnimationFrame(() => {
            inputRefs.current[i + splitLines.length - 1]?.focus();
        });
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>, i: number) => {
        const isEmpty = lines[i] === '';

        if (e.key === 'Backspace' && isEmpty && i > 0) {
            e.preventDefault();
            inputRefs.current[i - 1]?.focus();
            return;
        }

        if (e.key === 'Enter') {
            e.preventDefault();
            const isAtMax = lines.length >= maxRows;

            if (!isAtMax) {
                const updated = [...lines];
                updated.splice(i + 1, 0, '');
                onChange(updated.join('\n'));

                requestAnimationFrame(() => {
                    inputRefs.current[i + 1]?.focus();
                });
            } else if (i < maxRows - 1) {
                requestAnimationFrame(() => {
                    inputRefs.current[i + 1]?.focus();
                });
            }
        }

        if (e.key === 'ArrowUp' && i > 0) {
            e.preventDefault();
            inputRefs.current[i - 1]?.focus();
        }

        if (e.key === 'ArrowDown' && i < lines.length - 1) {
            e.preventDefault();
            inputRefs.current[i + 1]?.focus();
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
                        maxLength={maxCharsPerLine + 20}
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
