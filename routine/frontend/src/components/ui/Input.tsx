import React from 'react';

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {}

const Input: React.FC<Props> = ({ className = '', ...rest }) => {
    return (
        <input
            {...rest}
            className={`border-2 border-black h-[36px] bg-inherit px-3 text-sm rounded-none focus:outline-none ${className}`}
        />
    );
};

export default Input;
