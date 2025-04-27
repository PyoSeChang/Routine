// hooks/useRoutineDetailPage.ts

import { useState, useEffect } from 'react';
import axios from 'axios';
import { RoutineDTO } from '../types/routineDetail';

export function useRoutineDetail(routineId: number) {
    const [routine, setRoutine] = useState<RoutineDTO | null>(null);
    const [loading, setLoading] = useState(true);
    const [commitMessages, setCommitMessages] = useState<string[]>([]);
    const [commitRates, setCommitRates] = useState<any[]>([]);
    const [commitLoading, setCommitLoading] = useState(true);
    const [rateLoading, setRateLoading] = useState(true);

    useEffect(() => {
        async function fetchRoutine() {
            try {
                const res = await axios.get(`/api/routine/${routineId}`);
                setRoutine(res.data);
            } catch (error) {
                console.error('루틴 상세 조회 실패:', error);
            } finally {
                setLoading(false);
            }
        }
        fetchRoutine();
    }, [routineId]);

    return { routine, loading };
}
