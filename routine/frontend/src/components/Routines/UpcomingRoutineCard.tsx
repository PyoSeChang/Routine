import React from 'react';
import { RoutineViewDTO } from '../../types/routine';
import RepeatDays from '../ui/RepeatDays';
import TaskCheckBox from '../ui/TaskCheckBox';

const PastRoutineCard: React.FC<{ routine: RoutineViewDTO }> = ({ routine }) => {
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

                {/* 태스크 리스트 */}
                <div className="overflow-visible mb-2">
                    {routine.tasks.map(task => (
                        <div key={task.taskId} className="flex items-center h-[30px] w-full border-b border-blue-300">
                            <div className="pl-8 w-full flex items-center">
                                <TaskCheckBox
                                    label={task.content}
                                    checked={task.status === 'SUCCESS'}
                                    disabled={true} // 무조건 disabled
                                    onChange={() => {}} // onChange는 필요없음
                                />
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default PastRoutineCard;
