import React from 'react';
import { useNavigate } from 'react-router-dom';
import { RoutineViewDTO } from '../types/routine';

const TodayRoutineCard: React.FC<{ routine: RoutineViewDTO }> = ({ routine }) => {
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const form = e.currentTarget;
        const formData = new FormData(form);

        // ✅ submit된 버튼의 value를 action으로 추가
        const action = (document.activeElement as HTMLButtonElement)?.value;
        formData.append('action', action || 'save');

        try {
            const res = await fetch(`/commit/${routine.routineId}`, {
                method: 'POST',
                body: formData,
            });

            if (res.ok) {
                navigate(0);
            } else {
                const text = await res.text();
                console.error('🚨 루틴 저장/제출 실패:', res.status, text);
                alert(`루틴 저장/제출 실패: ${res.status} - ${text}`);
            }
        } catch (err) {
            console.error('❌ 요청 자체 실패:', err);
            alert('네트워크 오류 또는 서버 다운');
        }
    };

    return (
        <form onSubmit={handleSubmit} className="border border-gray-300 p-4 rounded-md shadow-sm bg-white w-[250px]">
            <h4 className="font-semibold text-lg">{routine.title}</h4>

            {/* 오늘은 그냥 넘기기 */}
            <div className="mt-2 space-x-4">
                <label>
                    <input type="radio" name="isSkippedStr" value="false" defaultChecked />
                    진행하기
                </label>
                <label>
                    <input type="radio" name="isSkippedStr" value="true" />
                    오늘은 그냥 넘기기
                </label>
            </div>

            {/* 태스크 반복 */}
            <div className="mt-2 space-y-1">
                {routine.tasks.map((task, i) => (
                    <div key={task.taskId}>
                        {/* ✅ taskId */}
                        <input
                            type="hidden"
                            name={`tasks[${i}].taskId`}
                            value={task.taskId}
                        />
                        {/* ✅ content */}
                        <input
                            type="hidden"
                            name={`tasks[${i}].content`}
                            value={task.content}
                        />
                        {/* ✅ checked 상태 */}
                        <input
                            type="checkbox"
                            id={`task-${routine.routineId}-${i}`}
                            name={`tasks[${i}].checked`}
                            defaultChecked={task.checked}
                        />
                        <label htmlFor={`task-${routine.routineId}-${i}`} className="ml-2">
                            {task.content}
                        </label>
                    </div>
                ))}
            </div>

            <div className="flex justify-end gap-2 mt-3">
                <button type="submit" name="action" value="save" className="btn btn-secondary">
                    저장
                </button>
                <button type="submit" name="action" value="submit" className="btn btn-primary">
                    제출
                </button>
            </div>
        </form>
    );
};

export default TodayRoutineCard;
