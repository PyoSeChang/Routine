// ChalkInput.tsx
import React from 'react';

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {}

const ChalkInput: React.FC<Props> = (props) => {
    return (
        <input
            {...props}
            className={`
        text-white
        px-4 py-2
        border-2 border-white
        bg-transparent
        rounded-md
        placeholder-white
        focus:outline-none
        focus:ring-2 focus:ring-white
        shadow-[0_0_1px_white,0_0_2px_white]
        font-ui
        ${props.className || ''}
      `}
            style={{
                textShadow: '0 0 1px white, 0 0 2px white',
                ...props.style,
            }}
        />
    );
};

export default ChalkInput;
