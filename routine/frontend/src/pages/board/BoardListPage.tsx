// src/pages/BoardListPage.tsx
import React, { useState } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import AppLayout from '../../layout/AppLayout';
import PostItBoardNav from '../../components/Board/PostItBoardNav';
import { useBoardList } from '../../hooks/useBoardList';
import { BoardListDTO } from '../../types/board';
import Input from "../../components/ui/Input";

export default function BoardListPage() {
    const [searchParams, setSearchParams] = useSearchParams();
    const category       = searchParams.get('category')       || undefined;
    const detailCategory = searchParams.get('detailCategory') || undefined;
    const keyword        = searchParams.get('keyword')        || undefined;
    const page           = parseInt(searchParams.get('page')   || '0', 10);
    const size           = parseInt(searchParams.get('size')   || '10', 10);

    const { boards, totalPages, loading } = useBoardList(
        category, detailCategory, keyword, page, size
    );
    const [error, setError] = useState<string | null>(null);

    const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const kw = (e.currentTarget.keyword as HTMLInputElement).value;
        setSearchParams({
            category: category || '',
            detailCategory: detailCategory || '',
            keyword: kw,
            page: '0',
            size: size.toString(),
        });
    };

    if (loading) return <p className="p-4">로딩중…</p>;
    if (error)   return <p className="p-4 text-red-500">에러: {error}</p>;

    return (
        <AppLayout>
                <div className="shrink-0 mr-6">
                    <PostItBoardNav />
                </div>
                <div className="flex-1">
                    <div className="max-w-4xl mx-auto">
                        <h1 className="text-2xl font-bold mb-4">게시판 목록</h1>

                        {/* 검색 */}
                        <form onSubmit={handleSearch} className="flex mb-4">
                            <Input
                                type="text"
                                name="keyword"
                                defaultValue={keyword}
                                placeholder="검색어"
                                className="flex-1"
                            />
                            <button
                                type="submit"
                                className="ml-2 px-4 py-2 bg-blue-500 text-white rounded"
                            >검색</button>
                        </form>

                        {/* 글쓰기 */}
                        <div className="flex justify-end mb-4">
                            <Link
                                to="/boards/write"
                                className="px-4 py-2 bg-blue-500 text-white rounded"
                            >글쓰기</Link>
                        </div>

                        {boards.length === 0 ? (
                            <p className="text-center text-gray-500">게시물이 없습니다.</p>
                        ) : (
                            <table className="w-full table-auto border-collapse">
                                <thead>
                                <tr className="bg-gray-100">
                                    <th className="border px-4 py-2">제목</th>
                                    <th className="border px-4 py-2">작성자</th>
                                    <th className="border px-4 py-2">카테고리</th>
                                    <th className="border px-4 py-2">수정일</th>
                                    <th className="border px-4 py-2">조회수</th>
                                </tr>
                                </thead>
                                <tbody>
                                {boards.map((b: BoardListDTO) => (
                                    <tr key={b.boardId} className="hover:bg-gray-50">
                                        <td className="border px-4 py-2">
                                            <Link
                                                to={`/boards/${b.boardId}`}
                                                className="text-blue-600 hover:underline"
                                            >{b.title}</Link>
                                        </td>
                                        <td className="border px-4 py-2">{b.writerName}</td>
                                        <td className="border px-4 py-2">
                                            {b.category} / {b.detailCategory}
                                        </td>
                                        <td className="border px-4 py-2">{b.modifiedDate}</td>
                                        <td className="border px-4 py-2">{b.viewCount}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        )}

                        {/* 페이징 */}
                        {totalPages > 1 && (
                            <div className="flex justify-center mt-4 space-x-2">
                                <button
                                    disabled={page <= 0}
                                    onClick={() => setSearchParams({
                                        category: category || '',
                                        detailCategory: detailCategory || '',
                                        keyword: keyword || '',
                                        page: (page - 1).toString(),
                                        size: size.toString(),
                                    })}
                                    className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
                                >이전</button>

                                <span className="px-3 py-1">
                                    {page + 1} / {totalPages}
                                </span>

                                <button
                                    disabled={page + 1 >= totalPages}
                                    onClick={() => setSearchParams({
                                        category: category || '',
                                        detailCategory: detailCategory || '',
                                        keyword: keyword || '',
                                        page: (page + 1).toString(),
                                        size: size.toString(),
                                    })}
                                    className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
                                >다음</button>
                            </div>
                        )}
                    </div>
                </div>
        </AppLayout>
    );
}