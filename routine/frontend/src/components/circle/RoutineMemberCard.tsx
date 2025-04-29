// src/components/circle/RoutineMemberCard.tsx
import React from 'react';
import { TaskDTO } from '../../types/circle';

interface Props {
    nickname: string;
    tasks: TaskDTO[];
}

const RoutineMemberCard: React.FC<Props> = ({ nickname, tasks }) => {
    const tasksWithPadding = [...tasks];
    while (tasksWithPadding.length < 10) {
        tasksWithPadding.push({
            taskId: -tasksWithPadding.length - 1,
            content: '',
            status: 'NONE',
            checked: false,
        });
    }

    return (
        <div className="relative p-0 rounded-md shadow-sm w-[420px]" style={{ backgroundColor: '#e3f2fd' }}>
            <div
                className="absolute top-0"
                style={{ left: '1.5rem', width: '2px', height: '100%', backgroundColor: '#42a5f5' }}
            />

            <div className="flex flex-col flex-1">
                {/* 멤버 닉네임 */}
                <div className="pb-2 mb-2 flex justify-center mt-4">
                    <span className="font-semibold text-lg text-blue-700">{nickname}</span>
                </div>

                {/* 커밋 없는 경우 안내 */}
                {tasks.every(task => task.status === 'NONE') && (
                    <div className="text-xs text-gray-400 flex justify-center mb-2">
                        오늘 커밋된 이력이 없습니다
                    </div>
                )}

                {/* 태스크 리스트 */}
                <div className="overflow-visible mb-2">
                    {tasksWithPadding.map(task => (
                        <div
                            key={task.taskId}
                            className="flex items-center h-[30px] w-full border-b border-blue-300"
                        >
                            <div className="pl-8 w-full flex items-center">
                                {task.content && (
                                    <>
                                        <input
                                            type="checkbox"
                                            checked={task.status === 'SUCCESS'}
                                            disabled
                                            className="form-checkbox"
                                        />
                                        <span
                                            className={`ml-2 relative inline-block ${task.status === 'SUCCESS' ? 'after:w-full' : 'after:w-0'}`}
                                            style={{ transition: 'all 0.3s ease', position: 'relative' }}
                                        >
                      {task.content}
                                            <span
                                                className="absolute left-0 bottom-1/2 h-px bg-black transition-all duration-300"
                                                style={{ transform: 'translateY(50%)', width: task.status === 'SUCCESS' ? '100%' : '0%' }}
                                            />
                    </span>
                                    </>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default RoutineMemberCard;
