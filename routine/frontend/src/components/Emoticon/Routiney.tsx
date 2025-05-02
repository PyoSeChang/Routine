// components/Emoticon/Routiney.tsx
import React from 'react';

const Routiney: React.FC = () => {
    return (
        <div className="w-fit">
            {/*<img*/}
            {/*    src="/assets/img/noisy.png"*/}
            {/*    alt="떠든 사람"*/}
            {/*    className="mx-auto"*/}
            {/*/>*/}
            <img
                src="/assets/img/girl.png"
                alt="떠든사람 루틴이"
                className="mx-auto"
                style={{
                    width: '192px',
                    transform: 'scale(2)',
                    transformOrigin: 'bottom left',
                }}
            />
        </div>
    );
};

export default Routiney;
