// components/Routine/RoutineCommitRates.tsx

import React from 'react';
import RoutineWeeklyLineChart from './RoutineWeeklyLineChart';
import RoutineTaskWeeklyChart from './RoutineTaskWeeklyChart';
import { RoutineCommitRatesResponse } from '../../types/routineRates'; // 타입 위치 조정

interface RoutineCommitRatesProps {
    data: RoutineCommitRatesResponse;
}

const RoutineCommitRates: React.FC<RoutineCommitRatesProps> = ({ data }) => {
    return (
        <div className="space-y-10">
            {/* 1번: 이번 주 / 저번 주 그래프 */}
            <RoutineWeeklyLineChart
                thisWeekRates={data.thisWeekDailyRates}
                lastWeekRates={data.lastWeekDailyRates}
            />

            {/* 2번: 주차별 루틴/태스크별 이행률 그래프 */}
            <RoutineTaskWeeklyChart
                routineWeeklyRates={data.routineWeeklyRates}
                taskWeeklyRates={data.taskWeeklyRates}
            />
        </div>
    );
};

export default RoutineCommitRates;
