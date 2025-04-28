import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from '../api/axios';
import { BoardDTO, Category, BoardType } from '../types/board';
import { detailCategories } from '../types/detailCategories';

interface Props {
    mode: 'create' | 'edit';
}

function BoardFormPage({ mode }: Props) {
    const { boardId } = useParams();
    const navigate = useNavigate();

    const [form, setForm] = useState<BoardDTO>({
        writer: 0,
        title: '',
        content: '',
        tags: '',
        category: Category.LANGUAGE,
        detailCategory: '',
        boardType: BoardType.NOTICE,
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
    };

    useEffect(() => {
        if (mode === 'edit' && boardId) {
            axios.get(`/boards/${boardId}`)
                .then(res => {
                    const data = res.data;
                    setForm({
                        writer: data.writerId,
                        title: data.title,
                        content: data.content,
                        tags: data.tags,
                        category: data.category,
                        detailCategory: data.detailCategory,
                        boardType: data.boardType,
                    });
                })
                .catch(err => {
                    console.error('게시글 불러오기 실패', err);
                });
        }
    }, [mode, boardId]);

    const handleSubmit = async () => {
        try {
            if (mode === 'create') {
                await axios.post('/boards', form);
            } else {
                await axios.put(`/boards/${boardId}`, form);
            }
            navigate('/boards');
        } catch (error) {
            const err = error as any;
            console.error('저장 실패', err);
            console.error('서버 전체 응답 데이터:', err.response?.data);
        }
    };

    const filteredDetailCategories = detailCategories.filter(
        (detail) => detail.parentCategory === form.category
    );

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">
                {mode === 'create' ? '새 글 작성' : '글 수정'}
            </h1>

            <div className="flex flex-col gap-4">

                {/* 1. 제목 */}
                <input
                    type="text"
                    name="title"
                    value={form.title}
                    onChange={handleChange}
                    placeholder="제목"
                    className="border p-2 rounded"
                />

                {/* 2. 카테고리 선택 */}
                <select
                    name="category"
                    value={form.category}
                    onChange={handleChange}
                    className="border p-2 rounded"
                >
                    <option value={Category.LANGUAGE}>외국어</option>
                    <option value={Category.EMPLOYMENT}>취업</option>
                    <option value={Category.STUDY}>학습</option>
                    <option value={Category.LIFE}>라이프</option>
                </select>

                {/* 3. 디테일카테고리 선택 */}
                <select
                    name="detailCategory"
                    value={form.detailCategory}
                    onChange={handleChange}
                    className="border p-2 rounded"
                >
                    <option value="">세부 카테고리를 선택하세요</option>
                    {filteredDetailCategories.map((detail) => (
                        <option key={detail.code} value={detail.code}>
                            {detail.displayName}
                        </option>
                    ))}
                </select>

                <select
                    name="boardType"
                    value={form.boardType}
                    onChange={handleChange}
                    className="border p-2 rounded"
                >
                    <option value={BoardType.NOTICE}>공지</option>
                    <option value={BoardType.PROMOTION}>홍보</option>
                    <option value={BoardType.REVIEW}>후기</option>
                    <option value={BoardType.QNA}>Q&A</option> {/* ⭐ 추가 */}
                    <option value={BoardType.INFORMATION}>정보공유</option> {/* ⭐ 추가 */}
                </select>

                {/* 4. 내용 */}
                <textarea
                    name="content"
                    value={form.content}
                    onChange={handleChange}
                    placeholder="내용"
                    className="border p-2 rounded h-40"
                />

                {/* 5. 태그 */}
                <input
                    type="text"
                    name="tags"
                    value={form.tags}
                    onChange={handleChange}
                    placeholder="태그(쉼표로 구분)"
                    className="border p-2 rounded"
                />

                {/* 제출 버튼 */}
                <button
                    onClick={handleSubmit}
                    className="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600"
                >
                    {mode === 'create' ? '등록' : '수정'}
                </button>

            </div>
        </div>
    );
}

export default BoardFormPage;
