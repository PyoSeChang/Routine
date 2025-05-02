// components/Emoticon/Sun.tsx
import React from 'react';

const Sun: React.FC = () => {
    return (
        <div className="w-fit">
            <img
                src="/assets/img/sun.png"
                alt="햇님"
                className="mx-auto"
                style={{
                    width: '150px', // or adjust as needed
                    transform: 'scale(2)',
                    transformOrigin: 'top right',
                }}
            />
        </div>
    );
};

export default Sun;
