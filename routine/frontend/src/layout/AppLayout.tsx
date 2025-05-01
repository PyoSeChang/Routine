import React from 'react';

interface Props {
    children: React.ReactNode;
}

const AppLayout: React.FC<Props> = ({ children }) => {
    return (
        <div className="flex justify-center">
            <div className="flex w-full max-w-[1200px] px-4">
                {children}
            </div>
        </div>
    );
};

export default AppLayout;
