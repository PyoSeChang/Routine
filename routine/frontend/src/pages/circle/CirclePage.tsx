// src/pages/Circle/CirclePage.tsx

import { Link } from 'react-router-dom';

export default function CirclePage() {
    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">내 서클</h1>

            {/* 서클 목록 (나중에 map 돌릴 자리) */}
            <div className="border p-4 rounded mb-6">
                <h2 className="text-lg font-semibold mb-2">가입한 서클 목록</h2>
                <div>서클 리스트 출력 예정</div>
            </div>

            {/* 가입/초대 현황 */}
            <div className="border p-4 rounded mb-6">
                <h2 className="text-lg font-semibold mb-2">가입 및 초대 현황</h2>
                <div>초대/가입 현황 출력 예정</div>
            </div>

            {/* 서클 만들기 버튼 */}
            <div className="text-right">
                <Link to="/circles/create">
                    <button className="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600">
                        서클 만들기
                    </button>
                </Link>
            </div>
        </div>
    );
}
