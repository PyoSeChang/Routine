import { useState } from 'react';
import axios from 'axios';
import CommitSummaryCard from './CommitSummaryCard'; // ✨ 새로 만든 카드 컴포넌트

interface TaskDTO {
    taskId: number;
    content: string;
    status: string;
    checked: boolean;
}

interface CommitLogDTO {
    routineName: string;
    tasks: TaskDTO[];
}

interface RoutineCommitSummaryProps {
    routineId: number;
    commitDates: string[];
}

export default function RoutineCommitSummary({ routineId, commitDates }: RoutineCommitSummaryProps) {
    const [selectedDate, setSelectedDate] = useState<string>('');
    const [commitLog, setCommitLog] = useState<CommitLogDTO | null>(null);

    const handleFetchCommitLog = async () => {
        if (!selectedDate) return;
        try {
            const res = await axios.get(`/api/routine/${routineId}/commit`, {
                params: { date: selectedDate }
            });
            setCommitLog(res.data);
        } catch (error) {
            console.error('커밋 로그 조회 실패', error);
        }
    };

    return (
        <div className="space-y-4">
            {/* 날짜 선택 영역 */}
            <div className="flex items-center gap-3">
                <select
                    value={selectedDate}
                    onChange={(e) => setSelectedDate(e.target.value)}
                    className="border p-2 rounded"
                    disabled={commitDates.length === 0}
                >
                    <option value="">날짜를 선택하세요</option>
                    {commitDates.map((date) => (
                        <option key={date} value={date}>
                            {date}
                        </option>
                    ))}
                </select>
                <button
                    onClick={handleFetchCommitLog}
                    className="bg-blue-500 text-white px-4 py-2 rounded disabled:opacity-50"
                    disabled={!selectedDate}
                >
                    조회
                </button>
            </div>

            {/* 커밋 결과 출력 */}
            <div className="pt-4">
                {commitLog ? (
                    <CommitSummaryCard title={commitLog.routineName} tasks={commitLog.tasks} />
                ) : (
                    <p className="text-gray-400 text-center">조회한 커밋 결과가 없습니다.</p>
                )}
            </div>
        </div>
    );
}
