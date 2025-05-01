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
                className={`relative w-full h-[36px] bg-note 
                            before:content-[''] before:absolute before:left-[2.5rem] before:top-0 
                            before:h-full before:w-[2px] before:bg-[#f28b82] before:z-20 
                            ${className}`}
            >
                {/* 하단 파란 선 */}
                <div className="absolute bottom-0 left-0 w-full h-px bg-blue-300" />

                {/* 본문 내용 들여쓰기 */}
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
