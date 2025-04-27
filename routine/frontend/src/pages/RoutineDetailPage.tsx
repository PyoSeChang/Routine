import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { useRoutineDetail } from '../hooks/useRoutineDetail';
import RoutineCommitMessages from '../components/Routine/RoutineCommitMessage';
import RoutineCommitRates from '../components/Routine/RoutineCommitRates';
import RoutineEditOverlay from '../components/Routine/RoutineEditOverlay';
import RoutineCommitSummary from "../components/Routine/RoutineCommitSummary";
import { RoutineCommitRatesResponse } from '../types/routineRates';

export default function RoutineDetailPage() {
    const { routineId } = useParams<{ routineId: string }>();
    const { routine, loading } = useRoutineDetail(Number(routineId));

    const [commitMessages, setCommitMessages] = useState<string[]>([]);
    const [commitRates, setCommitRates] = useState<RoutineCommitRatesResponse | null>(null);
    const [commitDates, setCommitDates] = useState<string[]>([]);

    const [messagesLoading, setMessagesLoading] = useState(true);
    const [isEditOpen, setIsEditOpen] = useState(false);
    const [openSection, setOpenSection] = useState<string | null>(null);

    useEffect(() => {
        if (!routineId) return;

        const fetchAll = async () => {
            try {
                const [messagesRes, ratesRes, datesRes] = await Promise.all([
                    axios.get(`/api/routine/${routineId}/messages`),
                    axios.get(`/api/routine/${routineId}/rates`),
                    axios.get(`/api/routine/${routineId}/commit-dates`)
                ]);
                setCommitMessages(messagesRes.data);
                setCommitRates(ratesRes.data);
                setCommitDates(datesRes.data);
            } catch (error) {
                console.error('루틴 상세 데이터 조회 실패', error);
            } finally {
                setMessagesLoading(false);
            }
        };

        fetchAll();
    }, [routineId]);

    if (loading) return <p className="text-center mt-10">루틴 로딩 중...</p>;
    if (!routine) return <p className="text-center mt-10">루틴을 찾을 수 없습니다.</p>;

    return (
        <div className="flex max-w-6xl mx-auto p-6 gap-8">
            {/* 왼쪽 앵커 메뉴 */}
            <nav className="sticky top-24 flex flex-col gap-4 text-blue-600">
                <a href="#commit-summary" className="hover:underline">커밋 살펴보기</a>
                <a href="#commit-stats" className="hover:underline">커밋 통계 확인하기</a>
                <a href="#commit-messages" className="hover:underline">커밋 메세지 모아보기</a>
                <a href="#routine-edit" className="hover:underline">루틴 수정</a>
            </nav>

            {/* 오른쪽 본문 */}
            <div className="flex-1 space-y-6 scroll-smooth">
                {/* 루틴 기본 정보 */}
                <div>
                    <h2 className="text-3xl font-bold mb-2">{routine.title}</h2>
                    <p className="text-gray-600 mb-4">{routine.description || '설명이 없습니다.'}</p>
                    <div className="text-sm text-gray-500 space-y-1">
                        <p>카테고리: {routine.category} / {routine.detailCategory}</p>
                        <p>반복 요일: {routine.repeatDays.join(', ')}</p>
                        <p>생성일: {routine.createdAt}</p>
                    </div>
                </div>

                {/* Section: 커밋 살펴보기 */}
                <section id="commit-summary" className="border p-4 rounded-lg shadow">
                    <div
                        className="cursor-pointer font-bold text-xl"
                        onClick={() => setOpenSection(openSection === 'commit-summary' ? null : 'commit-summary')}
                    >
                        커밋 살펴보기
                    </div>
                    {openSection === 'commit-summary' && (
                        <div className="mt-4">
                            <RoutineCommitSummary routineId={Number(routineId)} commitDates={commitDates} />
                        </div>
                    )}
                </section>

                {/* Section: 커밋 통계 보기 */}
                <section id="commit-stats" className="border p-4 rounded-lg shadow">
                    <div
                        className="cursor-pointer font-bold text-xl"
                        onClick={() => setOpenSection(openSection === 'commit-stats' ? null : 'commit-stats')}
                    >
                        커밋 통계 확인하기
                    </div>
                    {openSection === 'commit-stats' && (
                        <div className="mt-4">
                            {commitRates ? (
                                <RoutineCommitRates data={commitRates} />
                            ) : (
                                <p className="text-gray-500 text-sm">이행률 데이터를 불러오는 중입니다...</p>
                            )}
                        </div>
                    )}
                </section>

                {/* Section: 커밋 메세지 보기 */}
                <section id="commit-messages" className="border p-4 rounded-lg shadow">
                    <div
                        className="cursor-pointer font-bold text-xl"
                        onClick={() => setOpenSection(openSection === 'commit-messages' ? null : 'commit-messages')}
                    >
                        커밋 메세지 모아보기
                    </div>
                    {openSection === 'commit-messages' && (
                        <div className="mt-4">
                            <RoutineCommitMessages data={commitMessages} loading={messagesLoading} />
                        </div>
                    )}
                </section>

                {/* Section: 루틴 수정 */}
                <section id="routine-edit" className="border p-4 rounded-lg shadow">
                    <div
                        className="cursor-pointer font-bold text-xl"
                        onClick={() => setOpenSection(openSection === 'routine-edit' ? null : 'routine-edit')}
                    >
                        루틴 수정
                    </div>
                    {openSection === 'routine-edit' && (
                        <div className="mt-4">
                            <RoutineEditOverlay routine={routine} onClose={() => setIsEditOpen(false)} />
                        </div>
                    )}
                </section>
            </div>
        </div>
    );
}
