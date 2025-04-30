import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from '../api/axios';
import { BoardDTO, Category, BoardType } from '../types/board';
import InputOnNote from '../components/ui/InputOnNote';
import TextareaOnNote from '../components/ui/TextareaOnNote';
import CategoryOnNote from '../components/ui/CategoryOnNote';
import TagInputOnNote from '../components/ui/TagInputOnNote';
import BoardTypeSelectorOnNote from '../components/ui/BoardTypeSelectorOnNote';
import NoneLine from '../components/ui/NoneLine';
import BlankLine from "../components/ui/BlankLine";

interface Props {
    mode: 'create' | 'edit';
}

export default function BoardFormOnNotePage({ mode }: Props) {
    const { boardId } = useParams();
    const navigate = useNavigate();

    const [form, setForm] = useState<BoardDTO>({
        writer: 0,
        title: '',
        content: '',
        tags: '',
        category: Category.NONE,
        detailCategory: '',
        boardType: BoardType.NOTICE,
    });

    useEffect(() => {
        if (mode === 'edit' && boardId) {
            axios.get(`/boards/${boardId}`).then((res) => {
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
        } catch (err: any) {
            console.error('저장 실패', err);
            console.error('서버 전체 응답 데이터:', err.response?.data);
        }
    };

    return (
        <div className=" max-w-5xl mx-auto mt-10">
            <div className="max-w-4xl mx-auto bg-note relative">
                <div className="absolute top-0 z-[999]" style={{ left: '1.5rem', width: '2px', height: '100%', backgroundColor: '#f28b82' }} />
                <NoneLine/>
                <h1 className="text-xl font-bold font-ui text-center mb-4">
                    {mode === 'create' ? '새 글 작성' : '글 수정'}
                </h1>
                <NoneLine/>
                <InputOnNote
                    label="제목: "
                    value={form.title}
                    placeholder="제목을 입력해주세요."
                    onChange={(value) => setForm((prev) => ({ ...prev, title: value }))}
                />
                <NoneLine/>
                <CategoryOnNote
                    category={form.category}
                    detailCategory={form.detailCategory}
                    onCategoryChange={(category) =>
                        setForm((prev) => ({ ...prev, category, detailCategory: '' }))
                    }
                    onDetailCategoryChange={(detailCategory) =>
                        setForm((prev) => ({ ...prev, detailCategory }))
                    }
                />
                <BlankLine/>
                <BoardTypeSelectorOnNote
                    value={form.boardType}
                    onChange={(value) => setForm((prev) => ({ ...prev, boardType: value }))}
                />
                <BlankLine/>
                <TextareaOnNote
                    label="내용: "
                    value={form.content}
                    onChange={(value) => setForm((prev) => ({ ...prev, content: value }))}
                    placeholder="내용을 입력하세요"
                    maxRows={10}
                />
                <TagInputOnNote
                    tags={form.tags ? form.tags.split(',') : []}
                    setTags={(tags) =>
                        setForm((prev) => ({ ...prev, tags: tags.join(',') }))
                    }
                />

                <NoneLine />
                <button
                    onClick={handleSubmit}
                    className="bg-note py-2 w-full text-center"
                >
                    {mode === 'create' ? '등록' : '수정'}
                </button>
            </div>
        </div>
    );
}
