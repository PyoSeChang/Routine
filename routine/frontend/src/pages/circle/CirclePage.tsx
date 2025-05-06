// src/pages/Circle/CircleSearchPage.tsx

import React, { useEffect, useState } from 'react';
import axios from '../../api/axios';
import { CategorySelector } from '../../components/ui/CategorySelector';
import { Link } from 'react-router-dom';
import { Category } from '../../types/board';
import PostItExam from "../../components/ui/post_it/PostItSVG";
import AppLayout from "../../layout/AppLayout";
import ChalkButton from "../../components/ui/chalk/ChalkButton";
import ChalkInput from "../../components/ui/chalk/ChalkInput";
import ChalkLink from "../../components/ui/chalk/ChalkLink";

interface CircleSummary {
    circleId: number;
    name: string;
    category: string;
    detailCategory: string;
    currentMemberCount: number;
    maxMemberCount: number;
    createdAt: string;
    isLeader?: boolean;
    joinedAt?: string;
    tags: string[] | null;
    description: string[] | null;
}

export default function CircleSearchPage() {
    const [category, setCategory] = useState<Category>(Category.LANGUAGE);
    const [detailCategory, setDetailCategory] = useState<string>('');
    const [keyword, setKeyword] = useState('');
    const [results, setResults] = useState<CircleSummary[]>([]);
    const [myCircles, setMyCircles] = useState<CircleSummary[]>([]);

    const fetchMyCircles = async () => {
        try {
            const res = await axios.get('/circles');
            setMyCircles(res.data);
        } catch (err) {
            console.error('내 서클 조회 실패', err);
        }
    };

    const handleSearch = async () => {
        try {
            const res = await axios.get('/circles/search', {
                params: {
                    category: category || null,
                    detailCategory: detailCategory || null,
                    keyword: keyword || null,
                },
            });
            setResults(res.data);
        } catch (err) {
            console.error('서클 검색 실패', err);
        }
    };

    useEffect(() => {
        fetchMyCircles();
        handleSearch();
    }, []);

    return (
        <AppLayout>
            <div className="w-full max-w-[1000px] mx-auto p-6">
                <div>


                    {/* ✅ 내 서클 목록 */}
                    <div className="space-y-3 p-6">
                        <h1 className="text-[40px] font-chalk text-white mb-4">My Circles</h1>
                        {myCircles.map((circle) => (
                            <Link
                                to={`/circles/${circle.circleId}`}
                                key={circle.circleId}
                                className="block bg-white border border-gray-400 rounded-md hover:bg-gray-200 transition-all"
                            >
                                <div className="p-4 flex items-center">
                                    <div className="w-1/5 font-bold text-black flex items-center gap-1">
                                        {circle.name}
                                        {circle.isLeader && <span title="리더">👑</span>}
                                    </div>
                                    <div className="w-1/5 text-sm text-gray-700">
                                        {circle.category} / {circle.detailCategory}
                                    </div>
                                    <div className="w-1/5 text-sm text-gray-600">
                                        {circle.currentMemberCount} / {circle.maxMemberCount}
                                    </div>
                                    <div className="w-2/5 text-xs text-right text-gray-400">
                                        가입일: {circle.joinedAt && new Date(circle.joinedAt).toLocaleDateString('ko-KR')}
                                    </div>
                                </div>
                            </Link>
                        ))}
                    </div>

                    {/* ✅ 검색 필터 */}
                    <h1 className="text-[40px] font-chalk text-white p-6">Find Circle</h1>

                    <div className=" p-6">
                        <CategorySelector
                            category={category}
                            detailCategory={detailCategory}
                            onCategoryChange={setCategory}
                            onDetailCategoryChange={setDetailCategory}
                        />
                    </div>

                    <div className="pr-6 pl-6 flex gap-2">

                        <ChalkInput
                            type="text"
                            value={keyword}
                            onChange={(e) => setKeyword(e.target.value)}
                            placeholder="서클 이름 또는 태그 입력"
                            className="border p-2 rounded flex-1"
                        />
                        <ChalkButton
                            onClick={handleSearch}
                            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                            검색
                        </ChalkButton>
                    </div>

                    {/* ✅ 검색 결과 */}
                    {results.length === 0 ? (
                        <p className="text-gray-500 p-6">검색 결과가 없습니다.</p>
                    ) : (
                        <div className="space-y-3 p-6">
                            {results.map((circle) => (
                                <div
                                    key={circle.circleId}
                                    className="p-4 border rounded flex justify-between items-center bg-white"
                                >
                                    <Link
                                        to={`/circles/${circle.circleId}`}
                                        className="font-semibold text-blue-600 hover:underline w-1/4 truncate"
                                    >
                                        {circle.name}
                                    </Link>
                                    <div className="text-sm text-gray-700 w-1/4">
                                        {circle.category} / {circle.detailCategory}
                                    </div>
                                    <div className="text-sm text-gray-600 w-1/4">
                                        {circle.currentMemberCount} / {circle.maxMemberCount}
                                    </div>
                                    <div className="text-xs text-gray-400 w-1/4 text-right">
                                        생성일:{' '}
                                        {new Date(circle.createdAt).toLocaleString('ko-KR', {
                                            year: 'numeric',
                                            month: '2-digit',
                                            day: '2-digit',
                                            hour: '2-digit',
                                            minute: '2-digit',
                                            hour12: true,
                                        })}
                                    </div>
                                </div>
                            ))}
                        </div>

                    )}
                    <div className="flex justify-center">
                        <ChalkLink
                            to="/circles/create"
                        >서클 만들기</ChalkLink>
                    </div>
                </div>


            </div>
        </AppLayout>

    );
}
