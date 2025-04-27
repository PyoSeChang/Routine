// components/Routine/RoutineWeeklyLineChart.tsx

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
import { DailyRateDTO } from '../../types/routineRates'; // 타입 경로 조정

interface RoutineWeeklyLineChartProps {
    thisWeekRates: DailyRateDTO[];
    lastWeekRates: DailyRateDTO[];
}

const RoutineWeeklyLineChart: React.FC<RoutineWeeklyLineChartProps> = ({ thisWeekRates, lastWeekRates }) => {
    const [currentWeek, setCurrentWeek] = useState<'this' | 'last'>('this');

    const handlePrev = () => setCurrentWeek('last');
    const handleNext = () => setCurrentWeek('this');

    const data = currentWeek === 'this' ? thisWeekRates : lastWeekRates;

    return (
        <div className="w-full space-y-4">
            <div className="flex justify-center gap-4 items-center">
                <button onClick={handlePrev} className="px-3 py-1 text-sm bg-gray-300 rounded hover:bg-gray-400">
                    ◀️
                </button>
                <p className="text-lg font-semibold">{currentWeek === 'this' ? '이번 주 이행률' : '저번 주 이행률'}</p>
                <button onClick={handleNext} className="px-3 py-1 text-sm bg-gray-300 rounded hover:bg-gray-400">
                    ▶️
                </button>
            </div>

            <div className="w-full h-64">
                <ResponsiveContainer width="100%" height="100%">
                    <LineChart data={data}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="date" />
                        <YAxis
                            domain={[0, 1]}
                            tickFormatter={(value) => `${(value * 100).toFixed(0)}%`}
                        />
                        <Tooltip formatter={(value: number) => `${(value * 100).toFixed(0)}%`} />
                        <Line type="monotone" dataKey="commitRate" stroke="#82ca9d" />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default RoutineWeeklyLineChart;
