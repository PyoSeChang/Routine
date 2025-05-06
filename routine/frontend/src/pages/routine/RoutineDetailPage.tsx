import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import dayjs from 'dayjs';
import axios from '../../api/axios';
import { useRoutineDetail } from '../../hooks/useRoutineDetail';
import RoutineCommitMessages from '../../components/Routine/RoutineCommitMessage';
import RoutineCommitRates from '../../components/Routine/RoutineCommitRatesOnNote';
import RoutineEditOverlay from '../../components/Routine/RoutineEditOverlay';
import { RoutineCommitRatesResponse } from '../../types/routineRates';
import AppLayout from '../../layout/AppLayout';
import Line from '../../components/ui/note/Line';
import RepeatDaysOnNote from '../../components/ui/note/RepeatDaysOnNote';
import { Weekday } from '../../types/routine';
import DropdownOnNote from '../../components/ui/note/DropdownOnNote';
import BlankLine from "../../components/ui/note/BlankLine";
import { MessageDTO } from '../../types/message';
import NoteBlock from "../../components/ui/note/NoteBlock";
import CommitSummaryCard from '../../components/Routine/CommitSummaryCard';

interface TaskDTO {
    taskId: number;
    content: string;
    status: string;
    checked: boolean;
}

interface CommitLogDTO {
    routineName: string;
    tasks: TaskDTO[];
}

export default function RoutineDetailPage() {
    const { routineId } = useParams<{ routineId: string }>();
    const { routine, loading } = useRoutineDetail(Number(routineId));

    const [commitMessages, setCommitMessages] = useState<MessageDTO[]>([]);
    const [commitRates, setCommitRates] = useState<RoutineCommitRatesResponse | null>(null);
    const [commitDates, setCommitDates] = useState<string[]>([]);

    const [messagesLoading, setMessagesLoading] = useState(true);
    const [isEditOpen, setIsEditOpen] = useState(false);
    const [openSection, setOpenSection] = useState<string | null>(null);

    const [selectedDate, setSelectedDate] = useState<string>('');
    const [commitLog, setCommitLog] = useState<CommitLogDTO | null>(null);

    useEffect(() => {
        if (!routineId) return;

        const fetchAll = async () => {
            try {
                const [messagesRes, ratesRes, datesRes] = await Promise.all([
                    axios.get(`/routine/${routineId}/messages`),
                    axios.get(`/routine/${routineId}/rates`),
                    axios.get(`/routine/${routineId}/commit-dates`)
                ]);
                setCommitMessages(messagesRes.data);
                setCommitRates(ratesRes.data);
                setCommitDates(datesRes.data);
                console.log("✅ 커밋 이행률 데이터:", ratesRes.data);
            } catch (error) {
                console.error('루틴 상세 데이터 조회 실패', error);
            } finally {
                setMessagesLoading(false);
            }
        };

        fetchAll();
    }, [routineId]);

    const handleFetchCommitLog = async () => {
        if (!selectedDate) return;
        try {
            const res = await axios.get(`/routine/${routineId}/commit`, {
                params: { date: selectedDate }
            });
            setCommitLog(res.data);
        } catch (error) {
            console.error('커밋 로그 조회 실패', error);
        }
    };

    if (loading) return <p className="text-center mt-10">루틴 로딩 중...</p>;
    if (!routine) return <p className="text-center mt-10">루틴을 찾을 수 없습니다.</p>;

    return (
        <AppLayout>
            <div className="flex max-w-[800px] w-full mb-20 mx-auto gap-8">
                <div className="flex-1">
                    <div className="bg-blue-700 h-8 w-full rounded-t-lg shadow-md" />
                    <div className="bg-note shadow p-4">
                        <h2 className="text-3xl font-bold mb-2 text-center">{routine.title}</h2>

                        <BlankLine />

                        <Line>
                            <span className="font-bold">루틴 설명:</span> {routine.description || '설명이 없습니다.'}
                        </Line>

                        <RepeatDaysOnNote selectedDays={routine.repeatDays as Weekday[]} disabled />

                        <Line>
                            <span className="font-bold">카테고리:</span> {routine.category} / {routine.detailCategory}
                        </Line>

                        <Line>
                            <span className="font-bold">시작한 날짜:</span> {dayjs(routine.createdAt).format('YYYY.MM.DD')}
                        </Line>

                        <BlankLine />

                        <DropdownOnNote
                            value={selectedDate}
                            options={commitDates.map(date => ({ label: date, value: date }))}
                            onChange={setSelectedDate}
                            placeholder="날짜를 선택하세요"
                        />
                        <Line>
                            <button
                                onClick={handleFetchCommitLog}
                                disabled={!selectedDate}
                                className="text-black hover:underline justify-end"
                            >
                                [커밋 조회하기]
                            </button>
                        </Line>

                        {commitLog && (
                            <div>
                                <CommitSummaryCard title={commitLog.routineName} tasks={commitLog.tasks} />
                            </div>
                        )}

                        <BlankLine />

                        <Line>
                            <button
                                onClick={() => setOpenSection(openSection === 'commit-stats' ? null : 'commit-stats')}
                                className="hover:underline"
                            >
                                [커밋 통계 확인하기]
                            </button>
                        </Line>
                        {openSection === 'commit-stats' && (
                            <div>
                                {commitRates ? (
                                    <RoutineCommitRates data={commitRates} />
                                ) : (
                                    <p className="text-gray-500 text-sm">이행률 데이터를 불러오는 중입니다...</p>
                                )}
                            </div>
                        )}

                        <Line>
                            <button
                                onClick={() => setOpenSection(openSection === 'commit-messages' ? null : 'commit-messages')}
                                className="hover:underline"
                            >
                                [커밋 메세지 모아보기]
                            </button>
                        </Line>
                        {openSection === 'commit-messages' && (
                            <div>
                                <RoutineCommitMessages data={commitMessages} loading={messagesLoading} />
                            </div>
                        )}

                        <NoteBlock className="flex justify-center">
                            <div className="flex gap-4 mt-2">
                                {routine.circleRoutine ? (
                                    <>
                                        <button className="text-blue-600 hover:underline">서클로 가기</button>
                                        <button className="text-red-600 hover:underline">서클 탈퇴</button>
                                    </>
                                ) : (
                                    <>
                                        <button onClick={() => setIsEditOpen(true)} className="text-mainBlue hover:underline">
                                            루틴 수정
                                        </button>
                                        <button className="text-mainRed hover:underline">루틴 삭제</button>
                                        <button className="text-mainGreen hover:underline">루틴 공유</button>
                                    </>
                                )}
                            </div>
                        </NoteBlock>
                    </div>

                    {isEditOpen && <RoutineEditOverlay routine={routine} onClose={() => setIsEditOpen(false)} />}
                </div>
            </div>
        </AppLayout>
    );
}
