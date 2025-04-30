import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "../../api/axios";
import RoutinePublicCard from "../../components/circle/RoutinePublicCard";
import RoutineMemberCard from "../../components/circle/RoutineMemberCard";
import {
    MemberCommitInfoDTO,
    AuthorizationDTO,
    CircleMemberDTO
} from "../../types/circle";
import { RoutineViewDTO } from "../../types/routine";

export default function CircleDetailPage() {
    const { circleId } = useParams<{ circleId: string }>();

    const [routine, setRoutine] = useState<RoutineViewDTO | null>(null);
    const [memberCommits, setMemberCommits] = useState<MemberCommitInfoDTO[]>([]);
    const [authorization, setAuthorization] = useState<AuthorizationDTO | null>(null);
    const [circleMembers, setCircleMembers] = useState<CircleMemberDTO[]>([]);
    const [showMemberManage, setShowMemberManage] = useState(false);

    useEffect(() => {
        if (!circleId) return;

        const fetchCircleDetail = async () => {
            try {
                const res = await axios.get(`/circles/${circleId}`);
                const {
                    circleRoutine,
                    memberCommits: { memberCommits },
                    loginMember,
                    circleMembers
                } = res.data;

                setRoutine(circleRoutine);
                setMemberCommits(memberCommits);
                setAuthorization(loginMember);
                setCircleMembers(circleMembers);
            } catch (error) {
                console.error("서클 루틴 상세 불러오기 실패", error);
            }
        };

        fetchCircleDetail();
    }, [circleId]);

    const isLeader = authorization?.leader;
    const isMember = authorization?.member;

    const handleShowMemberManage = () => setShowMemberManage(true);

    const handleDelegateLeader = async (memberId: number) => {
        try {
            await axios.put(`/circles/${circleId}/members/${memberId}/assign-leader`);
            alert("리더가 위임되었습니다.");
            window.location.reload();
        } catch (err) {
            console.error("리더 위임 실패", err);
        }
    };

    const handleKickMember = async (memberId: number) => {
        try {
            await axios.delete(`/circles/${circleId}/members/${memberId}/remove`);
            alert("멤버를 추방했습니다.");
            setCircleMembers(prev => prev.filter(m => m.memberId !== memberId));
        } catch (err) {
            console.error("멤버 추방 실패", err);
        }
    };

    const renderMemberManagement = () => {
        if (!circleMembers || circleMembers.length === 0) {
            return <p className="text-gray-500 mt-4">등록된 멤버가 없습니다.</p>;
        }

        return (
            <div className="mt-4 space-y-2">
                {circleMembers.map(({ memberId, nickname }) => (
                    <div
                        key={memberId}
                        data-member-id={memberId}
                        className="flex justify-between items-center border px-4 py-2 rounded hover:bg-gray-50"
                    >
                        <span className="text-sm font-medium">{nickname}</span>
                        <div className="space-x-2">
                            <button
                                className="text-blue-500 hover:underline"
                                onClick={() => handleDelegateLeader(memberId)}
                            >
                                리더 위임
                            </button>
                            <button
                                className="text-red-500 hover:underline"
                                onClick={() => handleKickMember(memberId)}
                            >
                                추방
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        );
    };

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">서클 상세</h1>

            {!isMember && (
                <div className="mb-4">
                    <button
                        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                        onClick={async () => {
                            try {
                                await axios.post(`/circles/${circleId}/join`);
                                alert("서클에 가입되었습니다.");
                                window.location.reload();
                            } catch (error) {
                                console.error("가입 실패", error);
                                alert("가입에 실패했습니다.");
                            }
                        }}
                    >
                        가입하기
                    </button>
                </div>
            )}

            {routine ? (
                <div className="mb-8">
                    <RoutinePublicCard routine={routine} />
                </div>
            ) : (
                <p className="mb-8 text-gray-500">등록된 공용 루틴이 없습니다.</p>
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

            {isMember && !isLeader && (
                <div className="mt-10">
                    <button className="text-red-500 hover:underline"
                            onClick={async () => {
                                try {
                                    await axios.delete(`/circles/${circleId}/members/${authorization?.circleMemberId}/leave`);
                                    alert("서클에서 탈퇴했습니다.");
                                    window.location.href = "/circles";
                                } catch (err) {
                                    console.error("탈퇴 실패", err);
                                    alert("탈퇴에 실패했습니다.");
                                }
                            }}>탈퇴하기</button>
                </div>
            )}

            {isLeader && (
                <div className="mt-10">
                    <button
                        onClick={handleShowMemberManage}
                        className="text-blue-600 hover:underline"
                    >
                        멤버 관리하기
                    </button>
                    {showMemberManage && renderMemberManagement()}
                </div>
            )}
        </div>
    );
}
