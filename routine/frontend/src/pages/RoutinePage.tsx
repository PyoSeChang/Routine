import { RoutineViewDTO, Weekday } from '../types/routine';
import { useRoutineData } from '../hooks/useRoutineData';
import { weekdayLabels } from '../constants/weekdayLabels';
import RoutineSectionLayout from '../components/RoutineSectionLayout';

const RoutinePage = () => {
    const { routines, selectedWeekday, setSelectedWeekday } = useRoutineData();

    const filtered = routines.filter(r => r.weekday === selectedWeekday);
    const circleRoutines = filtered.filter(r => r.isGroupRoutine);      // ✅ 그룹 루틴
    const personalRoutines = filtered.filter(r => !r.isGroupRoutine);
    return (
        <div className="container mt-4">
            <h2 className="mb-4">요일별 루틴 보기</h2>

            <div className="d-flex gap-2 mb-4">
                {(Object.keys(weekdayLabels) as Weekday[]).map(day => (
                    <button
                        key={day}
                        className={`btn ${day === selectedWeekday ? 'btn-primary' : 'btn-outline-secondary'}`}
                        onClick={() => setSelectedWeekday(day)}
                    >
                        {weekdayLabels[day]}
                    </button>
                ))}
            </div>

            {filtered.length === 0 ? (
                <p>해당 요일의 루틴이 없습니다.</p>
            ) : (
                <RoutineSectionLayout
                    circleRoutines={circleRoutines}
                    personalRoutines={personalRoutines}
                />
            )}
        </div>
    );
};

export default RoutinePage;
