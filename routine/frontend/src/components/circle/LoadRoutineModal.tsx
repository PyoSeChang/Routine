import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface RoutineSummaryDTO {
    routineId: number;
    title: string;
}

interface Props {
    onClose: () => void;
    circleId: number;
}

const LoadRoutineModal: React.FC<Props> = ({ onClose, circleId }) => {
    const [routines, setRoutines] = useState<RoutineSummaryDTO[]>([]);
    const [selectedRoutineId, setSelectedRoutineId] = useState<number | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchRoutines = async () => {
            try {
                const res = await axios.get(`/api/circles/${circleId}/my-routines`);
                setRoutines(res.data);
            } catch (err: any) {
                console.error('루틴 목록 불러오기 실패', err);
                setError('루틴을 불러오지 못했습니다.');
            } finally {
                setLoading(false);
            }
        };
        fetchRoutines();
    }, [circleId]);

    const handleConfirm = async () => {
        if (selectedRoutineId == null) {
            alert('루틴을 선택해주세요');
            return;
        }
        try {
            await axios.post(`/api/circles/${circleId}/convert-routine`, {
                routineId: selectedRoutineId,
            });
            alert('루틴이 서클 루틴으로 변환되었습니다!');
            window.location.reload();
        } catch (err) {
            console.error('루틴 변환 실패', err);
            alert('루틴 변환에 실패했습니다.');
        }
    };

    if (loading) {
        return (
            <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30 z-50">
                <div className="bg-white p-6 rounded shadow text-center">로딩 중...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30 z-50">
                <div className="bg-white p-6 rounded shadow text-center text-red-500">{error}</div>
            </div>
        );
    }

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30 z-50">
            <div className="bg-white p-8 rounded-lg w-[400px] shadow-lg relative">
                <button className="absolute top-2 right-2 text-gray-500 hover:text-gray-700" onClick={onClose}>✕</button>
                <h2 className="text-2xl font-bold mb-6 text-center">루틴 불러오기</h2>

                {routines.length === 0 ? (
                    <p className="text-center">불러올 수 있는 루틴이 없습니다.</p>
                ) : (
                    <div className="space-y-2 mb-6">
                        {routines.map(routine => (
                            <div
                                key={routine.routineId}
                                className={`border rounded p-3 cursor-pointer ${selectedRoutineId === routine.routineId ? 'bg-blue-100 border-blue-400' : 'hover:bg-gray-100'}`}
                                onClick={() => setSelectedRoutineId(routine.routineId)}
                            >
                                {routine.title}
                            </div>
                        ))}
                    </div>
                )}

                <button
                    onClick={handleConfirm}
                    className="w-full bg-blue-500 text-white rounded-lg p-2 hover:bg-blue-600"
                    disabled={selectedRoutineId == null}
                >
                    확인
                </button>
            </div>
        </div>
    );
};

export default LoadRoutineModal;
