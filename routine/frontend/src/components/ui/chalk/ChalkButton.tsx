// ChalkButton.tsx
import React from 'react';

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    children: React.ReactNode;
    className?: string;
}

const ChalkButton: React.FC<Props> = ({ children, className = '', ...props }) => {
    const isDisabled = props.disabled;

    return (
        <div
            className={`ml-2 inline-block rounded ${isDisabled ? 'opacity-50 cursor-not-allowed' : ''}`}
            style={{
                backgroundImage: "url('/assets/textures/chalk-scribble-1.png')",
                backgroundRepeat: 'repeat',
                backgroundSize: 'cover',
                padding: '6px',
            }}
        >
            <div className="rounded border-2 border-white">
                <button
                    {...props}
                    className={`
                        w-full
                        h-full
                        px-4 py-2
                        rounded
                        text-black
                        font-chalk
                        font-bold
                        bg-transparent
                        hover:bg-white/10
                        disabled:cursor-not-allowed
                        ${className}
                    `}
                    style={{
                        textShadow: '0 0 1px white, 0 0 2px white',
                        ...props.style,
                    }}
                >
                    {children}
                </button>
            </div>
        </div>
    );
};

export default ChalkButton;
