// src/pages/Circle/CircleSearchPage.tsx

import React, { useEffect, useState } from 'react';
import axios from '../../api/axios';
import { CategorySelector } from '../../components/ui/CategorySelector';
import { Link } from 'react-router-dom';
import { Category } from '../../types/board';
import PostItExam from "../../components/PostItSVG";

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
        <div className="p-6">
            <PostItExam>하이</PostItExam>
            <h1 className="text-2xl font-bold mb-6">서클</h1>

            {/* ✅ 내 서클 목록 */}
            <div className="mb-10">
                <h2 className="text-xl font-semibold mb-4">내 서클</h2>
                {myCircles.length === 0 ? (
                    <p className="text-gray-500">아직 가입한 서클이 없습니다.</p>
                ) : (
                    <div className="space-y-3">
                        {myCircles.map((circle) => (
                            <div
                                key={circle.circleId}
                                className="p-4 border rounded flex justify-between items-center"
                            >
                                <Link
                                    to={`/circles/${circle.circleId}`}
                                    className="font-bold text-blue-600 hover:underline w-1/4 truncate"
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
                                    가입일:{' '}
                                    {circle.joinedAt &&
                                        new Date(circle.joinedAt).toLocaleDateString('ko-KR')}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* ✅ 검색 필터 */}
            <h2 className="text-xl font-semibold mb-4">서클 검색</h2>

            <div className="mb-6">
                <CategorySelector
                    category={category}
                    detailCategory={detailCategory}
                    onCategoryChange={setCategory}
                    onDetailCategoryChange={setDetailCategory}
                />
            </div>

            <div className="mb-6 flex gap-2">
                <input
                    type="text"
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                    placeholder="서클 이름 또는 태그 입력"
                    className="border p-2 rounded flex-1"
                />
                <button
                    onClick={handleSearch}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    검색
                </button>
            </div>

            {/* ✅ 검색 결과 */}
            {results.length === 0 ? (
                <p className="text-gray-500">검색 결과가 없습니다.</p>
            ) : (
                <div className="space-y-3">
                    {results.map((circle) => (
                        <div
                            key={circle.circleId}
                            className="p-4 border rounded flex justify-between items-center"
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
            <div className="text-right my-4">
                <Link to="/circles/create">
                    <button className="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600">
                        서클 만들기
                    </button>
                </Link>
            </div>
        </div>
    );
}
