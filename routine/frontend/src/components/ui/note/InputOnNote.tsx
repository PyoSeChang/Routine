import React from 'react';
import Line from './Line';

interface InputOnNoteProps {
    label?: string;
    value?: string;
    onChange: (value: string) => void;
    placeholder?: string;
    type?: string;
}

const InputOnNote: React.FC<InputOnNoteProps> = ({
                                                     label = '',
                                                     value = '',
                                                     onChange,
                                                     placeholder = '',
                                                     type = 'text',
                                                 }) => {
    return (
        <Line className="gap-2 px-2">
            {label && (
                <span className="shrink-0 font-ui text-base whitespace-pre">{label} </span>
            )}
            <input
                type={type}
                value={value}
                placeholder={placeholder}
                onChange={(e) => onChange(e.target.value)}
                className="w-full bg-note font-user text-base focus:outline-none"
            />
        </Line>
    );
};

export default InputOnNote;
