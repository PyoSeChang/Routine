import React, { forwardRef } from 'react';

interface NoneLineProps {
    children?: React.ReactNode;
    className?: string;
    onClick?: React.MouseEventHandler<HTMLDivElement>;
}

const NoneLine = forwardRef<HTMLDivElement, NoneLineProps>(
    ({ children, className = '', onClick }, ref) => {
        return (
            <div
                ref={ref}
                onClick={onClick}
                className={`relative flex items-center h-[36px] w-full bg-note 
                    before:content-[''] before:absolute before:left-[2.5rem] before:top-0 
                    before:h-full before:w-[2px] before:bg-[#f28b82] before:z-20 ${className}`}
            >
                <div className="pl-20 pr-2 w-full">{children}</div>
            </div>
        );
    }
) as React.ForwardRefExoticComponent<
    NoneLineProps & React.RefAttributes<HTMLDivElement>
>;

NoneLine.displayName = 'NoneLine';

export default NoneLine;
