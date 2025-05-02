// SearchListOnNote.tsx
import React from 'react';
import { Link } from 'react-router-dom';
import Line from './Line';
import TagOnNote from './TagOnNote';
import { BoardListDTO } from '../../../types/board';

interface Props {
    boards: BoardListDTO[];
}

const SearchListOnNote: React.FC<Props> = ({ boards }) => {
    return (
        <div className="flex flex-col">
            {boards.map((board) => (
                <Line key={board.boardId} className="py-2 px-4">
                    <div className="flex flex-wrap w-full text-sm text-gray-800 gap-y-1">
                        <span className="w-1/6 h-9 flex items-center text-gray-500">{board.boardType}</span>
                        <span className="w-1/6 h-9 flex items-center text-gray-700">{board.writerName}</span>
                        <span className="w-1/6 h-9 flex items-center text-gray-700">{board.category} / {board.detailCategory}</span>
                        <Link
                            to={`/boards/${board.boardId}`}
                            className="w-1/6 h-9 flex items-center font-semibold text-blue-600 hover:underline truncate"
                        >
                            {board.title}
                        </Link>
                        <div className="w-1/6 h-9 flex items-center gap-2">
                            {typeof board.tags === 'string' && board.tags.trim() !== '' && (
                                board.tags.split(',').map((tag: string) => (
                                    <TagOnNote key={tag.trim()} tag={tag.trim()} />
                                ))
                            )}
                        </div>
                        <span className="w-1/6 h-9 flex items-center justify-end text-gray-400">
                            {board.modifiedDate}
                        </span>
                    </div>
                </Line>
            ))}
        </div>
    );
};

export default SearchListOnNote;
