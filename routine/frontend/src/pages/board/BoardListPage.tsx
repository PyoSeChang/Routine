// src/pages/BoardListPage.tsx
import React, { useState } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import AppLayout from '../../layout/AppLayout';
import BoardLayout from "../../layout/BoardLayout";
import PostItBoardNav from '../../components/Board/PostItBoardNav';
import { useBoardList } from '../../hooks/useBoardList';
import { BoardListDTO } from '../../types/board';
import ChalkInput from '../../components/ui/chalk/ChalkInput';
import ChalkButton from '../../components/ui/chalk/ChalkButton';
import ChalkLink from '../../components/ui/chalk/ChalkLink';
import SearchListOnNote from '../../components/ui/note/SearchListOnNote';

export default function BoardListPage() {
    const [searchParams, setSearchParams] = useSearchParams();
    const category = searchParams.get('category') || undefined;
    const detailCategory = searchParams.get('detailCategory') || undefined;
    const keyword = searchParams.get('keyword') || undefined;
    const page = parseInt(searchParams.get('page') || '0', 10);

    const { boards, totalPages, loading } = useBoardList(
        category,
        detailCategory,
        keyword,
        page,
        undefined // 👈 size 전달 안함 → @PageableDefault(size = 15) 사용됨
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
        });
    };

    if (loading) return <p className="p-4">로딩중…</p>;
    if (error) return <p className="p-4 text-red-500">에러: {error}</p>;

    return (
        <BoardLayout>
            <div className="shrink-0 mr-6 mt-[216px] mb-30">
                <PostItBoardNav />
            </div>
            <div className="w-full mx-auto">
                <h1 className="text-[52px] font-chalk text-white mb-4">Community</h1>

                {/* 검색 및 글쓰기 */}
                <form onSubmit={handleSearch} className="flex items-center gap-2 mb-16">
                    <ChalkInput
                        type="text"
                        name="keyword"
                        defaultValue={keyword}
                        placeholder="검색어"
                        className="flex-1"
                    />
                    <ChalkButton type="submit">검색</ChalkButton>
                    <ChalkLink to="/boards/write">글쓰기</ChalkLink>
                </form>

                {/* 게시판 리스트 */}
                {boards.length === 0 ? (
                    <p className="text-center text-gray-500">게시물이 없습니다.</p>
                ) : (
                    <SearchListOnNote boards={boards} />
                )}

                {/* 페이징 */}
                {totalPages > 1 && (
                    <div className="flex justify-center mt-4 space-x-2">
                        <ChalkButton
                            disabled={page <= 0}
                            onClick={() =>
                                setSearchParams({
                                    category: category || '',
                                    detailCategory: detailCategory || '',
                                    keyword: keyword || '',
                                    page: (page - 1).toString(),
                                })
                            }
                            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
                        >
                            Prev
                        </ChalkButton>

                        <span className="px-1 py-1 font-chalk text-white mt-3 pr-2">
                            {page + 1} / {totalPages}
                        </span>

                        <ChalkButton
                            disabled={page + 1 >= totalPages}
                            onClick={() =>
                                setSearchParams({
                                    category: category || '',
                                    detailCategory: detailCategory || '',
                                    keyword: keyword || '',
                                    page: (page + 1).toString(),
                                })
                            }
                            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
                        >
                            <span className="font-chalk">Next</span>
                        </ChalkButton>
                    </div>
                )}
            </div>
        </BoardLayout>
    );
}
