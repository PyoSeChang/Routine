import React, { useState } from 'react';
import { useRoutineData } from '../../hooks/useRoutineData';
import WeekdayNav from '../../components/Routines/WeekdayNav';
import PastRoutineCard from '../../components/Routines/PastRoutineCard';
import TodayRoutineCard from '../../components/Routines/TodayRoutineCard';
import UpcomingRoutineCard from '../../components/Routines/UpcomingRoutineCard';
import CreateRoutineOverlay from "../../components/Routines/CreateRoutineOverlay";
import PostIt from "../../components/ui/post_it/PostIt";
import AppLayout from '../../layout/AppLayout';
import { Weekday } from '../../types/routine';
import ChalkButton from "../../components/ui/chalk/ChalkButton";

type ViewMode = { type: 'DAY'; day: Weekday };

const RoutinePage: React.FC = () => {
    const { routines } = useRoutineData();
    const [viewMode, setViewMode] = useState<ViewMode>({ type: 'DAY', day: 'MONDAY' });

    const filtered = routines.filter(r => viewMode.type === 'DAY' && r.weekday === viewMode.day);

    const circleRoutines = filtered.filter(r => r.groupRoutine);
    const personalRoutines = filtered.filter(r => !r.groupRoutine);
    const [isCreateMode, setIsCreateMode] = useState(false);
    const routineCardMap = {
        PAST: PastRoutineCard,
        TODAY: TodayRoutineCard,
        UPCOMING: UpcomingRoutineCard,
    } as const;

    return (
        <AppLayout>
            <div className="space-y-6 p-4 w-full">
                <WeekdayNav viewMode={viewMode} setViewMode={setViewMode}/>
                <h3 className="font-chalk text-white text-[32px]">&lt;Circle Routine&gt;</h3>
                {/* ✅ 서클 루틴 (groupRoutine = true) */}
                {circleRoutines.length > 0 && (
                    <div className="flex flex-wrap gap-4 ">
                        {circleRoutines.map(routine => {
                            const RoutineCardComponent = routineCardMap[routine.type];
                            return (
                                <RoutineCardComponent key={routine.routineId} routine={routine} />
                            );
                        })}
                    </div>
                )}
                <h3 className="font-chalk text-white text-[32px]">&lt;My Routine&gt;</h3>
                {/* ✅ 개인 루틴 (groupRoutine = false) */}
                {personalRoutines.length > 0 && (
                    <div className="flex flex-wrap gap-4 ">
                        {personalRoutines.map(routine => {
                            const RoutineCardComponent = routineCardMap[routine.type];
                            return (
                                <RoutineCardComponent key={routine.routineId} routine={routine} />
                            );
                        })}
                    </div>
                )}
                {/* ✅ 루틴 만들기 버튼 */}
                <div className="flex justify-center mt-6">
                    <ChalkButton onClick={() => setIsCreateMode(true)}>
                        루틴 만들기
                    </ChalkButton>
                </div>

                {/* ✅ 루틴 생성 오버레이 */}
                {isCreateMode && (
                    <CreateRoutineOverlay
                        onClose={() => setIsCreateMode(false)}
                        onSave={(routineId, title) => {
                            // 여기에도 저장 로직 있으면 작성
                        }}
                    />
                )}
            </div>
        </AppLayout>
    );
};

export default RoutinePage;
