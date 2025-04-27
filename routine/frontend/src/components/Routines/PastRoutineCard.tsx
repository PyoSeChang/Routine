import React from 'react';
import { RoutineViewDTO } from '../../types/routine';
import RepeatDays from '../ui/RepeatDays';
import TodayRoutineCard from "./TodayRoutineCard";

const PastRoutineCard: React.FC<{ routine: RoutineViewDTO }> = ({ routine }) => {
    // ✅ 10개 채우기
    const tasksWithPadding = [...routine.tasks];
    while (tasksWithPadding.length < 10) {
        tasksWithPadding.push({
            taskId: -tasksWithPadding.length - 1,  // 음수 ID
            content: '',
            status: 'NONE',
        });
    }

    return (
        <div className="relative p-0 rounded-md shadow-sm w-[420px]" style={{ backgroundColor: '#fff9c4' }}>
            {/* 빨간 세로선 */}
            <div className="absolute top-0" style={{ left: '1.5rem', width: '2px', height: '100%', backgroundColor: '#f28b82' }} />

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

                {/* 반복 요일 */}
                <div className="pb-2 mb-2 flex justify-center">
                    <RepeatDays selectedDays={routine.repeatDays} disabled />
                </div>

                {/* 이력 없음 메시지 */}
                {routine.tasks.every(task => task.status === 'NONE') && (
                    <div className="text-xs text-gray-400 flex justify-center mb-2">
                        아직 실행된 이력이 없습니다
                    </div>
                )}

                {/* 태스크 리스트 */}
                <div className="overflow-visible mb-2">
                    {tasksWithPadding.map(task => (
                        <div key={task.taskId} className="flex items-center h-[30px] w-full border-b border-blue-300">
                            <div className="pl-8 w-full flex items-center">
                                {/* ✅ content 있을 때만 렌더링 */}
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
                                            style={{
                                                transition: 'all 0.3s ease',
                                                position: 'relative',
                                            }}
                                        >
                                            {task.content}
                                            <span
                                                className="absolute left-0 bottom-1/2 h-px bg-black transition-all duration-300"
                                                style={{
                                                    transform: 'translateY(50%)',
                                                    width: task.status === 'SUCCESS' ? '100%' : '0%',
                                                }}
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

export default PastRoutineCard;