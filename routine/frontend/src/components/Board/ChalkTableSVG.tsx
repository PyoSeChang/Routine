// ChalkTableSVG.tsx
import React from 'react';

const ChalkTableSVG: React.FC = () => {
    const columns = ['제목', '작성자', '카테고리', '수정일', '조회수'];
    const rowCount = 5;

    const cellWidth = 150;
    const cellHeight = 50;
    const tableWidth = columns.length * cellWidth;
    const tableHeight = (rowCount + 1) * cellHeight; // +1 for header

    return (
        <svg
            width={tableWidth}
            height={tableHeight}
            viewBox={`0 0 ${tableWidth} ${tableHeight}`}
            xmlns="http://www.w3.org/2000/svg"
            style={{ backgroundColor: 'transparent', display: 'block' }}
        >
            <defs>
                <filter id="chalk-line">
                    <feGaussianBlur stdDeviation="0.6" />
                </filter>
            </defs>

            {/* Draw grid with chalk blur filter */}
            {Array.from({ length: rowCount + 2 }).map((_, i) => (
                <line
                    key={`row-${i}`}
                    x1={0}
                    y1={i * cellHeight}
                    x2={tableWidth}
                    y2={i * cellHeight}
                    stroke="white"
                    strokeWidth={3}
                    filter="url(#chalk-line)"
                />
            ))}
            {Array.from({ length: columns.length + 1 }).map((_, i) => (
                <line
                    key={`col-${i}`}
                    x1={i * cellWidth}
                    y1={0}
                    x2={i * cellWidth}
                    y2={tableHeight}
                    stroke="white"
                    strokeWidth={3}
                    filter="url(#chalk-line)"
                />
            ))}

            {/* Headers */}
            {columns.map((label, i) => (
                <text
                    key={label}
                    x={i * cellWidth + cellWidth / 2}
                    y={cellHeight / 2 + 6}
                    fill="white"
                    fontSize="16"
                    textAnchor="middle"
                    alignmentBaseline="middle"
                    style={{ fontFamily: 'Cafe24SsurroundAir, sans-serif' }}
                >
                    {label}
                </text>
            ))}
        </svg>
    );
};

export default ChalkTableSVG;
