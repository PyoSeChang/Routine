// components/RoutineEditOverlay.tsx

import React from 'react';
import { RoutineDTO } from '../../types/routineDetail';

interface RoutineEditOverlayProps {
    routine: RoutineDTO;
    onClose: () => void;
}

const RoutineEditOverlay: React.FC<RoutineEditOverlayProps> = ({ routine, onClose }) => {
    return (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg w-full max-w-md">
                <h3 className="text-xl font-bold mb-4">루틴 수정</h3>

                {/* 여기에 수정 폼 넣을 수 있음 */}
                <p className="text-sm text-gray-700 mb-4">루틴 제목: {routine.title}</p>
                <p className="text-sm text-gray-700 mb-4">설명: {routine.description || '설명이 없습니다.'}</p>

                <button onClick={onClose} className="mt-4 px-4 py-2 bg-gray-300 rounded">
                    닫기
                </button>
            </div>
        </div>
    );
};

export default RoutineEditOverlay;
