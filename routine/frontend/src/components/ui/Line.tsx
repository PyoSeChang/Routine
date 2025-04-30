import React, { forwardRef } from 'react';

interface LineProps {
    children?: React.ReactNode;
    className?: string;
    onClick?: React.MouseEventHandler<HTMLDivElement>;
}

const Line = forwardRef<HTMLDivElement, LineProps>(
    ({ children, className = '', onClick }, ref) => {
        return (
            <div
                ref={ref}
                onClick={onClick}
                className={`relative w-full h-[36px] bg-note ${className}`}
            >
                {/* 줄은 절대위치로, 전체 꽉 채워서 */}
                <div className="absolute bottom-0 left-0 w-full h-px bg-blue-300" />

                {/* 내용은 들여쓰기해서 배치 */}
                <div className="flex items-center h-full pl-20 pr-2">
                    {children}
                </div>
            </div>
        );
    }
) as React.ForwardRefExoticComponent<
    LineProps & React.RefAttributes<HTMLDivElement>
>;

Line.displayName = 'Line';

export default Line;
