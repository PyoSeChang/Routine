import React, { useState } from 'react';
import AppLayout from '../../layout/AppLayout';
import Line from '../../components/ui/note/Line';
import InputOnNote from '../../components/ui/note/InputOnNote';
import axios from '../../api/axios';

interface PointLogDTO {
    reason: string;
    amount: number;
    status: string;
    failureReason: string | null;
    routineId: number | null;
    purchaseId: number | null;
    commitDate: string;
    createdAt: string;
}

const gridCols = "grid grid-cols-[15%_10%_10%_15%_15%_10%_10%_15%] w-full text-sm";
const cellBase = "h-9 flex items-center px-2";
const center = "justify-center";
const right = "justify-end";

const AdminPage: React.FC = () => {
    const [loginId, setLoginId] = useState('');
    const [pointLogs, setPointLogs] = useState<PointLogDTO[] | null>(null);

    const handleSearch = async () => {
        try {
            const res = await axios.get(`/member/admin/points/${loginId}`);
            setPointLogs(res.data);
        } catch (err) {
            console.error('포인트 로그 조회 실패', err);
            alert('로그인 아이디를 확인하세요.');
        }
    };

    return (
        <AppLayout>
            <div className="w-full max-w-[1200px] mx-auto">
                <Line>
                    <p className="text-bold text-center">포인트 로그 조회하기</p>
                </Line>
                <Line indent={false} className="gap-2">
                    <div className="flex-1">
                        <InputOnNote
                            label="로그인 아이디: "
                            value={loginId}
                            onChange={setLoginId}
                        />
                    </div>
                    <button
                        onClick={handleSearch}
                        className="text-sm text-blue-600 hover:underline shrink-0"
                    >
                        [검색하기]
                    </button>
                </Line>


                {pointLogs && (
                    <>
                        <Line>
                            <div className={`${gridCols} text-gray-700 font-semibold`}>
                                <span className={cellBase}>사유</span>
                                <span className={`${cellBase} ${center}`}>포인트</span>
                                <span className={`${cellBase} ${center}`}>상태</span>
                                <span className={`${cellBase} ${center}`}>실패 사유</span>
                                <span className={`${cellBase} ${center}`}>루틴 ID</span>
                                <span className={`${cellBase} ${center}`}>구매 ID</span>
                                <span className={`${cellBase} ${right}`}>커밋일</span>
                                <span className={`${cellBase} ${right}`}>생성일</span>
                            </div>
                        </Line>
                        {pointLogs.map((log, idx) => (
                            <Line key={idx}>
                                <div className={`${gridCols} text-gray-800`}>
                                    <span className={cellBase}>{log.reason}</span>
                                    <span className={`${cellBase} ${center}`}>+{log.amount}P</span>
                                    <span className={`${cellBase} ${center}`}>{log.status}</span>
                                    <span className={`${cellBase} ${center}`}>{log.failureReason ?? '-'}</span>
                                    <span className={`${cellBase} ${center}`}>{log.routineId ?? '-'}</span>
                                    <span className={`${cellBase} ${center}`}>{log.purchaseId ?? '-'}</span>
                                    <span className={`${cellBase} ${right} text-xs`}>{log.commitDate}</span>
                                    <span className={`${cellBase} ${right} text-xs`}>{log.createdAt}</span>
                                </div>
                            </Line>
                        ))}
                    </>
                )}
                <Line className="px-2">
                    <a
                        href="http://localhost:8080/product/register"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-sm text-blue-600 hover:underline"
                    >
                        [상품 등록 페이지로 이동]
                    </a>
                </Line>
            </div>
        </AppLayout>
    );
};

export default AdminPage;
