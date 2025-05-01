import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { RoutineViewDTO, TaskDTO } from '../../types/routine';
import RepeatDays from '../ui/RepeatDays';
import LinedTextArea from '../ui/note/LinedTextArea';
import TaskCheckBox from '../ui/TaskCheckBox';

type CommitStatus = 'SUCCESS' | 'FAIL' | 'SKIP' | 'NONE';
type TaskState = Record<number, CommitStatus>;

const TodayRoutineCard: React.FC<{ routine: RoutineViewDTO }> = ({ routine }) => {
    const navigate = useNavigate();

    const initialTaskStatusMap: TaskState = routine.tasks.reduce((acc, task) => {
        acc[task.taskId] = (task.status ?? 'NONE') as CommitStatus;
        return acc;
    }, {} as TaskState);

    const [taskStatusMap, setTaskStatusMap] = useState<TaskState>(initialTaskStatusMap);
    const [isSkippedStr, setIsSkippedStr] = useState<'true' | 'false'>('false');
    const [message, setMessage] = useState('');
    const [isPublic, setIsPublic] = useState(false);

    const handleToggleSuccess = (taskId: number, checked: boolean) => {
        setTaskStatusMap(prev => ({
            ...prev,
            [taskId]: checked ? 'SUCCESS' : 'NONE',
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const payload = {
            routineId: routine.routineId,
            skipped: isSkippedStr === 'true',
            taskStatuses: Object.entries(taskStatusMap).map(([taskId, status]) => ({
                taskId: Number(taskId),
                status,
            })),
            message: message.trim() !== '' ? message : null,
            isPublic: routine.groupRoutine ? isPublic : null,
        };

        try {
            const res = await fetch(`/api/commit/today`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload),
            });
            if (!res.ok) {
                const text = await res.text();
                console.error('🚨 루틴 저장 실패:', res.status, text);
                alert(`루틴 저장 실패: ${res.status} - ${text}`);
            }
        } catch (err) {
            console.error('❌ 요청 실패:', err);
            alert('네트워크 오류 또는 서버 다운');
        }
    };

    const tasksWithPadding: TaskDTO[] = [...routine.tasks];
    while (tasksWithPadding.length < 10) {
        tasksWithPadding.push({
            taskId: -tasksWithPadding.length - 1,
            content: '',
            status: 'NONE',
        });
    }

    return (
        <form
            onSubmit={handleSubmit}
            className="relative p-0 rounded-md shadow-sm w-[420px]"
            style={{ backgroundColor: '#fff9c4' }}
        >
            <div className="flex">
                {/* 빨간 세로선 */}
                <div className="absolute top-0" style={{ left: '1.5rem', width: '2px', height: '100%', backgroundColor: '#f28b82' }} />

                {/* 내용 */}
                <div className="flex flex-col flex-1">
                    <div className="pb-2 mb-2 flex justify-center">
                        <Link
                            to={`/routine/${routine.routineId}`}
                            className="font-semibold text-lg hover:underline block mt-4"
                        >
                            {routine.title}
                        </Link>
                    </div>

                    <div className="pb-2 mb-2 flex justify-center">
                        <RepeatDays selectedDays={routine.repeatDays} disabled />
                    </div>

                    <div className="pb-2 mb-2 text-sm pl-12">
                        {routine.groupRoutine && (
                            <p className="text-blue-500">스킵 가능 횟수: {routine.skipCount}회</p>
                        )}
                        <label className="flex items-center gap-2">
                            <input
                                type="checkbox"
                                checked={routine.routineSkipped || isSkippedStr === 'true'}
                                onChange={e => setIsSkippedStr(e.target.checked ? 'true' : 'false')}
                                disabled={routine.routineSkipped}
                                className="form-checkbox"
                            />
                            <span>오늘은 그냥 넘기기</span>
                        </label>
                    </div>

                    <div className="overflow-visible mb-2">
                        {tasksWithPadding.map(task => (
                            <div
                                key={task.taskId}
                                className="flex items-center h-[30px] w-full border-b border-blue-300"
                            >
                                <div className="pl-8 w-full">
                                    <TaskCheckBox
                                        label={task.content}
                                        checked={taskStatusMap[task.taskId] === 'SUCCESS'}
                                        disabled={task.content === '' || routine.routineSkipped || isSkippedStr === 'true' || taskStatusMap[task.taskId] === 'SUCCESS'}
                                        onChange={(checked) => handleToggleSuccess(task.taskId, checked)}
                                    />
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="pb-2 mb-2">
                        <LinedTextArea
                            value={message}
                            onChange={e => setMessage(e.target.value)}
                            placeholder="오늘 커밋에 대한 메세지를 남겨보세요!"
                            rows={3}
                        />
                    </div>

                    {routine.groupRoutine && (
                        <div className="flex items-center gap-2 pb-2 mb-2 pl-8">
                            <input
                                type="checkbox"
                                checked={isPublic}
                                onChange={(e) => setIsPublic(e.target.checked)}
                                className="form-checkbox"
                            />
                            <span className="text-sm text-gray-700">서클 멤버에게 공개하기</span>
                        </div>
                    )}

                    <div className="flex justify-center mt-2 mb-4">
                        <button type="submit" className="btn btn-primary">
                            저장
                        </button>
                    </div>
                </div>
            </div>
        </form>
    );
};

export default TodayRoutineCard;
