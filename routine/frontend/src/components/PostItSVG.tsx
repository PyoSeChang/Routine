import React from 'react';

interface Props {
    children?: React.ReactNode;
    className?: string;
    width?: number;
    height?: number;
    variant?: number; // 숫자로 색상 고르기
}

const COLOR_MAP: Record<number, string> = {
    0: '#fff9c4', // 기본 연노랑
    1: '#ffe0b2', // 주황
    2: '#f8bbd0', // 핑크
    3: '#c8e6c9', // 연두
    4: '#b3e5fc', // 하늘
    5: '#d1c4e9', // 보라
};

const PostItSVG: React.FC<Props> = ({
                                        children,
                                        className = '',
                                        width = 300,
                                        height = 100,
                                        variant = 0,
                                    }) => {
    const rise = 6;
    const color = COLOR_MAP[variant % Object.keys(COLOR_MAP).length] || COLOR_MAP[0];

    return (
        <div className={`relative ${className}`} style={{ width, height }}>
            <svg
                width={width}
                height={height}
                viewBox={`0 0 ${width} ${height}`}
                xmlns="http://www.w3.org/2000/svg"
                className="absolute top-0 left-0 z-0"
            >
                <defs>
                    <filter id="postitShadow" x="0" y="0" width="200%" height="200%">
                        <feDropShadow dx="20" dy="20" stdDeviation="6" floodColor="rgba(0,0,0,0.25)" />
                    </filter>
                </defs>

                <path
                    d={`M0 ${rise} 
                      C ${width * 0.25} ${rise * 0.66}, ${width * 0.75} ${rise * 0.33}, ${width} 0 
                      L ${width} ${height - rise} 
                      C ${width * 0.75} ${height - rise * 0.66}, ${width * 0.25} ${height - rise * 0.33}, 0 ${height} 
                      Z`}
                    fill={color}
                    filter="url(#postitShadow)"
                />
            </svg>

            <div className="relative z-10 w-full h-full p-4 flex items-center justify-center text-center">
                {children}
            </div>
        </div>
    );
};

export default PostItSVG;
