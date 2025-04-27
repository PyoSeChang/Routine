// components/Routine/RoutineTaskWeeklyChart.tsx

import React, { useState } from 'react';
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
    CartesianGrid,
} from 'recharts';
import { WeeklyRateDTO, TaskWeeklyRateDTO } from '../../types/routineRates'; // 타입 경로 조정

interface RoutineTaskWeeklyChartProps {
    routineWeeklyRates: WeeklyRateDTO[];
    taskWeeklyRates: TaskWeeklyRateDTO[];
}

const RoutineTaskWeeklyChart: React.FC<RoutineTaskWeeklyChartProps> = ({ routineWeeklyRates, taskWeeklyRates }) => {
    const [selectedTaskIds, setSelectedTaskIds] = useState<Set<number>>(new Set());

    const toggleTask = (taskId: number) => {
        setSelectedTaskIds(prev => {
            const next = new Set(prev);
            if (next.has(taskId)) {
                next.delete(taskId);
            } else {
                next.add(taskId);
            }
            return next;
        });
    };

    return (
        <div className="w-full space-y-4">
            <div className="flex flex-wrap gap-2">
                {taskWeeklyRates.map(task => (
                    <label key={task.taskId} className="flex items-center gap-1 text-sm">
                        <input
                            type="checkbox"
                            checked={selectedTaskIds.has(task.taskId)}
                            onChange={() => toggleTask(task.taskId)}
                        />
                        {task.taskContent}
                    </label>
                ))}
            </div>

            <div className="w-full h-64">
                <ResponsiveContainer width="100%" height="100%">
                    <LineChart>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="week" />
                        <YAxis
                            domain={[0, 1]}
                            tickFormatter={(value) => `${(value * 100).toFixed(0)}%`}
                        />
                        <Tooltip formatter={(value: number) => `${(value * 100).toFixed(0)}%`} />

                        {/* 루틴 전체 이행률 */}
                        <Line
                            data={routineWeeklyRates}
                            type="monotone"
                            dataKey="commitRate"
                            stroke="#8884d8"
                            name="루틴 평균"
                        />

                        {/* 선택된 태스크들 */}
                        {taskWeeklyRates.filter(task => selectedTaskIds.has(task.taskId)).map(task => (
                            <Line
                                key={task.taskId}
                                data={task.rates}
                                type="monotone"
                                dataKey="commitRate"
                                stroke="#ff7300"
                                name={task.taskContent}
                            />
                        ))}
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default RoutineTaskWeeklyChart;
