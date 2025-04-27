import { useEffect, useState } from 'react';
import axios from 'axios';
import { RoutineViewDTO, Weekday } from '../types/routine';

export const useRoutineData = () => {
    const [routines, setRoutines] = useState<RoutineViewDTO[]>([]);
    const [selectedWeekday, setSelectedWeekday] = useState<Weekday>('WEDNESDAY');

    useEffect(() => {
        axios.get('/api/routine/week')
            .then(res => setRoutines(res.data))
            .catch(err => console.error('루틴 불러오기 실패:', err));
    }, []);

    return { routines, selectedWeekday, setSelectedWeekday };
};
