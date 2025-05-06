import React from 'react';
import { RoutineViewDTO } from '../../types/routine';
import RepeatDays from '../ui/RepeatDays';
import { Pencil } from "lucide-react";

interface Props {
    routine: RoutineViewDTO;
    isLeader?: boolean;
}

const RoutinePublicCard: React.FC<Props> = ({ routine, isLeader }) => {
    const tasksWithPadding = [...routine.tasks];
    while (tasksWithPadding.length < 10) {
        tasksWithPadding.push({
            taskId: -tasksWithPadding.length - 1,
            content: '',
            status: 'NONE',
        });
    }

    return (
        <div
            className="relative p-0 rounded-md shadow-sm w-[420px]"
            style={{ backgroundColor: '#fff9c4' }}
        >
            <div
                className="absolute top-0"
                style={{
                    left: '1.5rem',
                    width: '2px',
                    height: '100%',
                    backgroundColor: '#f28b82',
                }}
            />

            <div className="flex flex-col flex-1">
                {/* 제목 */}
                <div className="pb-2 mb-2 flex justify-center mt-4">
                    <a
                        href={`/routine/${routine.routineId}`}
                        className="font-semibold text-lg hover:underline block"
                    >
                        {routine.title}
                    </a>
                </div>

                {/* 설명 */}
                <div className="text-center text-sm text-gray-600 mb-2 px-4">
                    <div className="flex justify-center items-center gap-1 text-base font-semibold text-gray-700">
                        <span>&lt;공지사항&gt;</span>
                        {isLeader && (
                            <button
                                className="text-blue-500 hover:text-blue-700"
                                onClick={() => alert("수정")}
                            >
                                <Pencil className="w-4 h-4" />
                            </button>
                        )}
                    </div>

                    {routine.description ? (
                        <div className="mt-1">{routine.description}</div>
                    ) : (
                        <div className="mt-1 text-gray-400 italic">
                            작성된 공지사항이 없습니다.
                        </div>
                    )}
                </div>


                {/* 반복 요일 */}
                <div className="pb-2 mb-2 flex justify-center">
                    <RepeatDays selectedDays={routine.repeatDays} disabled />
                </div>

                {/* 태스크 리스트 */}
                <div className="overflow-visible mb-2">
                    {tasksWithPadding.map(task => (
                        <div
                            key={task.taskId}
                            className="flex items-center h-[30px] w-full border-b border-blue-300"
                        >
                            <div className="pl-8 w-full flex items-center">
                                {task.content && (
                                    <span className="ml-2 inline-block">{task.content}</span>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default RoutinePublicCard;
