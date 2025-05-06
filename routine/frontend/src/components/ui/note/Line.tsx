import React, { forwardRef } from 'react';

interface LineProps {
    children?: React.ReactNode;
    className?: string;
    onClick?: React.MouseEventHandler<HTMLDivElement>;
    indent?: boolean | string; // true(default), false, or 'pl-16'
}

const Line = forwardRef<HTMLDivElement, LineProps>(
    ({ children, className = '', onClick, indent = true }, ref) => {
        let indentClass = '';
        if (typeof indent === 'string') {
            indentClass = indent;
        } else if (indent === true) {
            indentClass = 'pl-20';
        } // false면 '' 유지

        return (
            <div
                ref={ref}
                onClick={onClick}
                className={`relative w-full h-[36px] bg-note 
                    before:content-[''] before:absolute before:left-[2.5rem] before:top-0 
                    before:h-full before:w-[2px] before:bg-[#f28b82] before:z-20 
                    ${className}`}
            >
                <div className="absolute bottom-0 left-0 w-full h-px bg-blue-300" />

                {/* indentClass 적용 */}
                <div className={`flex items-center h-full ${indentClass} pr-2`}>
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
