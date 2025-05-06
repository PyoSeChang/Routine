// components/Routine/RoutineCommitRatesOnNote.tsx

import React from 'react';
import { RoutineCommitRatesResponse } from '../../types/routineRates';
// src/components/Routine/RoutineCommitRatesOnNote.tsx
import RoutineWeeklyRateOnNote from './RoutineWeeklyRateOnNote'; //
import RoutineTaskWeeklyRateOnNote from './RoutineTaskWeeklyRateOnNote';


interface RoutineCommitRatesOnNoteProps {
    data: RoutineCommitRatesResponse;
}

const RoutineCommitRatesOnNote: React.FC<RoutineCommitRatesOnNoteProps> = ({ data }) => {
    return (
        <div >
            {/* 1번: 이번 주 / 저번 주 이행률 그래프 */}
            <RoutineWeeklyRateOnNote
                thisWeekRates={data.thisWeekDailyRates}
                lastWeekRates={data.lastWeekDailyRates}
            />

            {/* 2번: 루틴/태스크별 주차별 이행률 그래프 */}
            <RoutineTaskWeeklyRateOnNote
                routineWeeklyRates={data.routineWeeklyRates}
                taskWeeklyRates={data.taskWeeklyRates}
            />
        </div>
    );
};

export default RoutineCommitRatesOnNote;
