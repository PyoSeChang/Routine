import React, { useState } from 'react';
import { useRoutineData } from '../hooks/useRoutineData';
import WeekdayNav from '../components/WeekdayNav';
import PastRoutineCard from '../components/PastRoutineCard';
import TodayRoutineCard from '../components/TodayRoutineCard';
import UpcomingRoutineCard from '../components/UpcomingRoutineCard';
import { Weekday } from '../types/routine';

type ViewMode = { type: 'DAY'; day: Weekday };

const RoutinePage: React.FC = () => {
    const { routines } = useRoutineData();
    const [viewMode, setViewMode] = useState<ViewMode>({ type: 'DAY', day: 'MONDAY' });

    const filtered = routines.filter(r => viewMode.type === 'DAY' && r.weekday === viewMode.day);

    return (
        <div className="space-y-6">
            <WeekdayNav viewMode={viewMode} setViewMode={setViewMode} />
            <div className="flex flex-wrap gap-4">
                {filtered.map(routine => {
                    switch (routine.type) {
                        case 'PAST': return <PastRoutineCard key={routine.routineId} routine={routine} />;
                        case 'TODAY': return <TodayRoutineCard key={routine.routineId} routine={routine} />;
                        case 'UPCOMING': return <UpcomingRoutineCard key={routine.routineId} routine={routine} />;
                    }
                })}
            </div>

        </div>

    );
};

export default RoutinePage;
