import React from 'react';
import { useNavigate } from 'react-router-dom';
import Line from './Line';
import TagOnNote from './TagOnNote';
import { BoardListDTO } from '../../../types/board';
import NoteBlock from './NoteBlock';
import NoneLine from './NoneLine';

interface Props {
    boards: BoardListDTO[];
}

const gridCols = "grid grid-cols-[10%_15%_10%_21%_21%_13%_9%] w-full text-sm";
const cellBase = "h-9 flex items-center px-2";
const center = "justify-center";
const right = "justify-end";

const SearchListOnNote: React.FC<Props> = ({ boards }) => {
    const navigate = useNavigate();
    const remaining = 15 - boards.length;

    return (
        <div className="flex flex-col">
            <div className="bg-blue-700 h-6 w-full rounded-t-md shadow-lg " />
            <NoneLine />
            <Line className="bg-yellow-100 border-b border-gray-300">
                <div className={`${gridCols} text-gray-700 font-semibold`}>
                    <span className={cellBase}>말머리</span>
                    <span className={cellBase}>카테고리</span>
                    <span className={cellBase}>작성자</span>
                    <span className={cellBase}>제목</span>
                    <span className={cellBase}>태그</span>
                    <span className={`${cellBase} ${center}`}>작성일</span>
                    <span className={`${cellBase} ${right}`}>조회수</span>
                </div>
            </Line>

            {boards.map((board) => (
                <Line
                    key={board.boardId}
                    onClick={() => navigate(`/boards/${board.boardId}`)}
                    className="cursor-pointer hover:bg-yellow-50 transition-colors duration-150"
                >
                    <div className={`${gridCols} text-gray-800`}>
                        <span className={`${cellBase} text-gray-500`}>{board.boardType}</span>
                        <span className={cellBase}>{board.category} / {board.detailCategory}</span>
                        <span className={cellBase}>{board.writerName}</span>
                        <span className={`${cellBase} font-semibold text-blue-600 truncate`}>{board.title}</span>
                        <div className={`${cellBase} gap-2 overflow-hidden whitespace-nowrap`}>
                            {typeof board.tags === 'string' && board.tags.trim() !== '' && (
                                board.tags.split(',').map((tag: string) => (
                                    <TagOnNote key={tag.trim()} tag={tag.trim()} />
                                ))
                            )}
                        </div>
                        <span className={`${cellBase} ${center} text-gray-400`}>{board.modifiedDate}</span>
                        <span className={`${cellBase} ${right} text-gray-400`}>{board.viewCount}</span>
                    </div>
                </Line>
            ))}

            {/* 빈 줄 채우기 */}
            {Array.from({ length: remaining > 0 ? remaining : 0 }).map((_, index) => (
                <Line key={`empty-${index}`}>
                    <div className={gridCols}>
                        {Array.from({ length: 7 }).map((__, i) => (
                            <span key={i} className={cellBase}>&nbsp;</span>
                        ))}
                    </div>
                </Line>
            ))}
            <NoneLine/>
            <NoneLine/>
        </div>
    );
};

export default SearchListOnNote;
