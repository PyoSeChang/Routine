// components/RoutineCommitMessages.tsx

import React from 'react';

interface RoutineCommitMessagesProps {
    data: string[];
    loading: boolean;
}

const RoutineCommitMessages: React.FC<RoutineCommitMessagesProps> = ({ data, loading }) => {
    if (loading) return <p className="text-sm text-gray-500">커밋 메시지 로딩 중...</p>;
    if (data.length === 0) return <p className="text-sm text-gray-500">커밋 메시지가 없습니다.</p>;

    return (
        <ul className="space-y-2">
            {data.map((msg, idx) => (
                <li key={idx} className="text-sm text-gray-700">{msg}</li>
            ))}
        </ul>
    );
};

export default RoutineCommitMessages;
