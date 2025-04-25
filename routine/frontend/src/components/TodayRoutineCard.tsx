import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { RoutineViewDTO } from '../types/routine';

type CommitStatus = 'SUCCESS' | 'FAIL' | 'SKIP' | 'NONE';
type TaskState = Record<number, CommitStatus>;

const TodayRoutineCard: React.FC<{ routine: RoutineViewDTO }> = ({ routine }) => {
    const navigate = useNavigate();

    const [taskStatusMap, setTaskStatusMap] = useState<TaskState>(
        Object.fromEntries(routine.tasks.map(task => [task.taskId, task.status ?? 'NONE']))
    );

    const [isSkippedStr, setIsSkippedStr] = useState<'true' | 'false'>('false');

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
        };

        try {
            const res = await fetch(`/api/commit/today`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            });
            console.log('📤 전송된 payload:', JSON.stringify(payload, null, 2));
            console.log(typeof payload.skipped);
            if (res.ok) {
                // navigate(0);
            } else {
                const text = await res.text();
                console.error('🚨 루틴 저장 실패:', res.status, text);
                alert(`루틴 저장 실패: ${res.status} - ${text}`);
            }
        } catch (err) {
            console.error('❌ 요청 실패:', err);
            alert('네트워크 오류 또는 서버 다운');
        }
    };

    return (
        <form onSubmit={handleSubmit} className="border border-gray-300 p-4 rounded-md shadow-sm bg-white w-[250px]">
            <h4 className="font-semibold text-lg mb-3">{routine.title}</h4>

            {/* ✅ 루틴 전체 스킵 체크박스 */}
            <div className="mt-2 text-sm">
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

            {/* ✅ 태스크 체크박스 - 성공만 체크 가능 */}
            <div className="mt-4 space-y-3">
                {routine.tasks.map((task) => {
                    const status = taskStatusMap[task.taskId];
                    const isDisabled =
                        routine.routineSkipped || isSkippedStr === 'true' || status === 'SUCCESS';

                    return (
                        <div key={task.taskId} className="flex items-center gap-2 text-sm">
                            <input
                                type="checkbox"
                                checked={status === 'SUCCESS'}
                                onChange={e => handleToggleSuccess(task.taskId, e.target.checked)}
                                disabled={isDisabled}
                                className="form-checkbox"
                            />
                            <label
                                className={`ml-1 ${status === 'SUCCESS' ? 'line-through text-blue-400' : ''}`}
                            >
                                {task.content}
                            </label>
                        </div>

                    );
                })}
            </div>
            {/* ✅ 저장 버튼 */}
            <div className="flex justify-end mt-5">
                <button type="submit" className="btn btn-primary">
                    저장
                </button>
            </div>
        </form>
    );
};

export default TodayRoutineCard;
