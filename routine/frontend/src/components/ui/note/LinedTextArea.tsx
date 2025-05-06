import React from 'react';

interface LinedTextAreaProps {
    value: string;
    onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
    placeholder?: string;
    rows?: number;
}

const LinedTextArea: React.FC<LinedTextAreaProps> = ({ value, onChange, placeholder, rows = 3 }) => {
    const lineHeight = 24; // px 단위 줄 높이 고정

    return (
        <textarea
            value={value}
            onChange={onChange}
            placeholder={placeholder}
            className="w-full bg-yellow-100 text-sm resize-none tracking-wide"
            style={{
                height: `${rows * 24}px`,    // ✅ rows 수 × 줄높이로 height 고정
                paddingTop: '2px',
                paddingBottom: '6px',
                paddingLeft: '48px',
                fontFamily: 'NanumBarunpen',
                fontSize: '14px',
                lineHeight: '24px',
                backgroundImage: `repeating-linear-gradient(
      to bottom,
      transparent,
      transparent 23px,
      #90cdf4 23px,
      #90cdf4 24px
    )`,
                backgroundSize: '100% 24px',
                border: 'none',
                boxShadow: 'none',
                outline: 'none',
            }}
        />
    );
};

export default LinedTextArea;
