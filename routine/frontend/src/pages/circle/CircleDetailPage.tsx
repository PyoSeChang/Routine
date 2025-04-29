// src/pages/circle/CircleDetailPage.tsx

import CreateRoutineOverlay from "../../components/Routines/CreateRoutineOverlay";
import RoutinePublicCard from "../../components/circle/RoutinePublicCard";
import RoutineMemberCard from "../../components/circle/RoutineMemberCard";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "../../api/axios";
import { MemberCommitInfoDTO } from "../../types/circle";
import { RoutineViewDTO } from "../../types/routine";

export default function CircleDetailPage() {
    const { circleId } = useParams<{ circleId: string }>();
    const [isCreateMode, setIsCreateMode] = useState(false);
    const [routine, setRoutine] = useState<RoutineViewDTO | null>(null);
    const [memberCommits, setMemberCommits] = useState<MemberCommitInfoDTO[]>([]);
    const [isLeader, setIsLeader] = useState(false);

    useEffect(() => {
        if (!circleId) return;

        const fetchCircleDetail = async () => {
            try {
                const res = await axios.get(`/circles/${circleId}`);
                setRoutine(res.data.circleRoutine); // ✅ 정확한 경로
                setMemberCommits(res.data.memberCommits.memberCommits); // ✅ 정확한 경로
                setIsLeader(res.data.isLeader); // ✅ 추가로 받은 거
            } catch (error) {
                console.error("서클 루틴 상세 불러오기 실패", error);
            }
        };

        fetchCircleDetail();
    }, [circleId]);


    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">서클 상세</h1>

            {routine ? (
                <div className="mb-8">
                    <RoutinePublicCard routine={routine} />
                </div>
            ) : (
                <>
                    {isLeader ? (
                        <div className="flex gap-4 mb-6">
                            <button
                                className="bg-purple-500 text-white px-6 py-2 rounded hover:bg-purple-600"
                                onClick={() => setIsCreateMode(true)}
                            >
                                루틴 만들기
                            </button>

                            <button className="bg-purple-500 text-white px-6 py-2 rounded hover:bg-purple-600">
                                루틴 불러오기
                            </button>
                        </div>
                    ) : (
                        <p className="mb-8 text-gray-500">등록된 공용 루틴이 없습니다.</p>
                    )}
                </>
            )}


            <div className="flex flex-wrap gap-6">
                {memberCommits.length === 0 ? (
                    <p className="text-gray-500">아직 오늘의 커밋 이력이 없습니다.</p>
                ) : (
                    memberCommits.map(info => (
                        <RoutineMemberCard
                            key={info.memberId}
                            nickname={info.nickname}
                            tasks={info.tasks}
                        />
                    ))
                )}
            </div>

            {isCreateMode && circleId && (
                <CreateRoutineOverlay
                    onClose={() => setIsCreateMode(false)}
                    onSave={(routineId, title) => {
                        // 필요하면 저장하거나, 아니면 빈 함수라도 둬야 한다
                    }}
                />
            )}
        </div>
    );
}
