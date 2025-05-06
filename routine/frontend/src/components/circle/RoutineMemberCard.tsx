import React from 'react';
import { TaskDTO } from '../../types/circle';
import PostItSVG from '../ui/post_it/PostItSVG';
import TaskCheckBox from "../ui/TaskCheckBox";

interface Props {
    nickname: string;
    tasks: TaskDTO[];
    commitMessage?: string;
    commitRate?: number;
}

const RoutineMemberCard: React.FC<Props> = ({ nickname, tasks, commitMessage, commitRate }) => {
    const isSkipped = tasks.some(task => task.status === 'SKIP');
    const isEmpty = tasks.every(task => task.status === 'NONE');

    // padding을 채운 task 배열
    const tasksWithPadding: TaskDTO[] = [
        ...tasks,
        ...Array.from({ length: Math.max(0, 10 - tasks.length) }, (_, i) => ({
            taskId: -(i + 1),
            content: '',
            status: 'NONE' as const,
            checked: false,
        })),
    ];

    return (
        <div
            className="relative p-0 rounded-md shadow-sm w-[420px]"
            style={{ backgroundColor: '#e3f2fd' }}
        >
            <div
                className="absolute top-0"
                style={{ left: '1.5rem', width: '2px', height: '100%', backgroundColor: '#42a5f5' }}
            />

            {isSkipped && (
                <>


                    <img
                        src="/assets/img/cancelled.png"
                        alt="Cancelled Stamp"
                        className="absolute top-[20%] left-[50%] w-[340px] -translate-x-1/2 opacity-80 rotate-[-20deg] pointer-events-none select-none"
                    />

                </>
            )}
            {commitRate !== undefined && commitRate >= 70 && (
                <img
                    src="/assets/img/completed.png"
                    alt="Success Stamp"
                    className="absolute top-[20%] left-[50%] w-[340px] -translate-x-1/2 opacity-80 rotate-[-20deg] pointer-events-none select-none"
                />
            )}


            <div className="flex flex-col flex-1">
                <div className="pb-2 mb-2 flex justify-center mt-4">
                    <span className="font-semibold text-lg text-blue-700">{nickname}</span>
                </div>


                <div className="overflow-visible mb-2">
                    {tasksWithPadding.map(task => (
                        <div
                            key={task.taskId}
                            className="flex items-center h-[30px] w-full border-b border-blue-300"
                        >
                            <div className="pl-8 w-full flex items-center">
                                {task.content && (
                                    <TaskCheckBox
                                        label={task.content}
                                        checked={task.status === 'SUCCESS'}
                                        disabled
                                        onChange={() => {}}
                                    />
                                )}

                            </div>
                        </div>
                    ))}


                        <div className="mt-4 px-6 pb-4">
                            <PostItSVG variant={0} width={360} height={120}>
                                <div className="w-full h-full flex flex-col items-start justify-start text-sm leading-snug text-gray-800 whitespace-pre-wrap">
                                    <div className="font-bold text-[13px] mb-1">오늘의 한 마디</div>
                                    <div className="pl-1 pr-2">{commitMessage}</div>
                                </div>
                            </PostItSVG>

                        </div>

                </div>

            </div>
        </div>
    );
};

export default RoutineMemberCard;
