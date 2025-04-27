import React from 'react';

interface PostItProps {
    content: string;
}

const PostIt: React.FC<PostItProps> = ({ content}) => {
    return (
        <div
            className="relative p-4 bg-purple-300 shadow-lg "
            style={{
                width: '250px',
                height: '60px', // 세로로 긴 직사각형
            }}
        >
            {/* 제목 */}
            <h3 className="text-2xl font-semibold text-black mb-3">{content}</h3>

        </div>
    );
};

export default PostIt;
