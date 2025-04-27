import React, { useState } from 'react';
import { useRoutineData } from '../hooks/useRoutineData';
import WeekdayNav from '../components/Routines/WeekdayNav';
import PastRoutineCard from '../components/Routines/PastRoutineCard';
import TodayRoutineCard from '../components/Routines/TodayRoutineCard';
import UpcomingRoutineCard from '../components/Routines/UpcomingRoutineCard';
import PostIt from "../components/ui/PostIt";
import { Weekday } from '../types/routine';

type ViewMode = { type: 'DAY'; day: Weekday };

const RoutinePage: React.FC = () => {
    const { routines } = useRoutineData();
    const [viewMode, setViewMode] = useState<ViewMode>({ type: 'DAY', day: 'MONDAY' });

    const filtered = routines.filter(r => viewMode.type === 'DAY' && r.weekday === viewMode.day);

    const circleRoutines = filtered.filter(r => r.groupRoutine);
    const personalRoutines = filtered.filter(r => !r.groupRoutine);

    const routineCardMap = {
        PAST: PastRoutineCard,
        TODAY: TodayRoutineCard,
        UPCOMING: UpcomingRoutineCard,
    } as const;

    return (
        <div className="space-y-6 p-4">
            <WeekdayNav viewMode={viewMode} setViewMode={setViewMode}/>
            <PostIt content="서클 루틴"/>
            {/* ✅ 서클 루틴 (groupRoutine = true) */}
            {circleRoutines.length > 0 && (
                <div className="flex flex-wrap gap-4">
                    {circleRoutines.map(routine => {
                        const RoutineCardComponent = routineCardMap[routine.type];
                        return (
                            <RoutineCardComponent key={routine.routineId} routine={routine} />
                        );
                    })}
                </div>
            )}
            <PostIt content="개인 루틴"/>
            {/* ✅ 개인 루틴 (groupRoutine = false) */}
            {personalRoutines.length > 0 && (
                <div className="flex flex-wrap gap-4">
                    {personalRoutines.map(routine => {
                        const RoutineCardComponent = routineCardMap[routine.type];
                        return (
                            <RoutineCardComponent key={routine.routineId} routine={routine} />
                        );
                    })}
                </div>
            )}
        </div>
    );
};

export default RoutinePage;
