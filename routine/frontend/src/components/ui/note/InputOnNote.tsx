import React from 'react';
import Line from './Line';

interface InputOnNoteProps {
    label?: string;
    value?: string;
    onChange: (value: string) => void;
    placeholder?: string;
    type?: 'text' | 'number' | 'radio' | 'password' | 'email';
    name?: string;
    checked?: boolean; // ✅ radio 전용
    indent?: boolean | string;
    onBlur?: (e: React.FocusEvent<HTMLInputElement>) => void;
    disabled?: boolean;
}

const InputOnNote: React.FC<InputOnNoteProps> = ({
                                                     label = '',
                                                     value = '',
                                                     onChange,
                                                     placeholder = '',
                                                     type = 'text',
                                                     name,
                                                     checked,
                                                     indent,
                                                     onBlur,
                                                     disabled
                                                 }) => {
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (type === 'radio') {
            onChange(e.target.value); // radio는 선택된 value 전달
        } else {
            onChange(e.target.value);
        }
    };

    return (
        <Line className="gap-2 px-2" indent={indent}>
            {label && (
                <span className="shrink-0 font-ui text-base whitespace-pre">{label} </span>
            )}
            <input
                type={type}
                name={name}
                value={value}
                checked={type === 'radio' ? checked : undefined}
                onChange={handleChange}
                onBlur={onBlur}
                placeholder={placeholder}
                className="w-full bg-note font-user text-base focus:outline-none"
                disabled={disabled}
            />
        </Line>
    );
};

export default InputOnNote;
