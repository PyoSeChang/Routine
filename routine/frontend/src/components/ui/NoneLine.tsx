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
                className={`flex items-center h-[36px] w-full bg-note ${className}`}
            >
                {children}
            </div>
        );
    }
) as React.ForwardRefExoticComponent<
    NoneLineProps & React.RefAttributes<HTMLDivElement>
>;

NoneLine.displayName = 'NoneLine';

export default NoneLine;
