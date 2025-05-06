import { useEffect, useState } from 'react';
import axios from '../api/axios'; // ✅ 수정: 커스텀 axios 인스턴스
import { RoutineViewDTO, Weekday } from '../types/routine';

export const useRoutineData = () => {
    const [routines, setRoutines] = useState<RoutineViewDTO[]>([]);
    const [selectedWeekday, setSelectedWeekday] = useState<Weekday>('WEDNESDAY');

    useEffect(() => {
        axios.get('/routine/week') // ❌ '/api/routine/week' 아님. baseURL이 '/api'라 생략
            .then(res => setRoutines(res.data))
            .catch(err => console.error('루틴 불러오기 실패:', err));
    }, []);

    return { routines, selectedWeekday, setSelectedWeekday };
};
