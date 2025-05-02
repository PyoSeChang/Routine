// ChalkLink.tsx
import React from 'react';
import { Link, LinkProps } from 'react-router-dom';

interface Props extends LinkProps {
    children: React.ReactNode;
    className?: string;
}

const ChalkLink: React.FC<Props> = ({ children, className = '', ...props }) => {
    return (
        <div
            className="ml-2 inline-block rounded"
            style={{
                backgroundImage: "url('/assets/textures/chalk-scribble-1.png')",
                backgroundRepeat: 'repeat',
                backgroundSize: 'cover',
                padding: '6px',
            }}
        >
            <div className="rounded border-2 border-white">
                <Link
                    {...props}
                    className={`
            w-full
            h-full
            inline-block
            px-4 py-2
            rounded
            text-black
            font-chalk
            font-bold
            bg-transparent
            hover:bg-white/10
            ${className}
          `}
                    style={{
                        textShadow: '0 0 1px white, 0 0 2px white',
                    }}
                >
                    {children}
                </Link>
            </div>
        </div>
    );
};

export default ChalkLink;
