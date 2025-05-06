// components/RoutineCommitMessages.tsx

import React from 'react';
import NoteBlock from "../ui/note/NoteBlock";
import PostItSVG from "../ui/post_it/PostItSVG";

interface MessageDTO {
    message: string;
    commitDate: string;
    colorId: number;
}

interface RoutineCommitMessagesProps {
    data: MessageDTO[];
    loading: boolean;
}

const RoutineCommitMessages: React.FC<RoutineCommitMessagesProps> = ({ data, loading }) => {
    if (loading) return <p className="text-sm text-gray-500">커밋 메시지 로딩 중...</p>;
    if (data.length === 0) return <p className="text-sm text-gray-500">커밋 메시지가 없습니다.</p>;

    return (
        <NoteBlock >
            <div className="mt-4 flex flex-col items-center gap-4 pl-20">
                {data.map((msg, idx) => (
                    <PostItSVG key={idx} width={300} height={100} variant={msg.colorId}>
                        <div className="text-sm text-gray-800">
                            <div className="font-semibold mb-1">{msg.commitDate}</div>
                            <div>{msg.message}</div>
                        </div>
                    </PostItSVG>
                ))}
            </div>

        </NoteBlock>
    );
};

export default RoutineCommitMessages;
