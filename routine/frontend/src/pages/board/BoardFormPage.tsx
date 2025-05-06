// src/pages/BoardFormOnNotePage.tsx
import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from '../../api/axios';
import { BoardDTO, Category, BoardType } from '../../types/board';
import InputOnNote from '../../components/ui/note/InputOnNote';
import TextareaOnNote from '../../components/ui/note/TextareaOnNote';
import CategoryOnNote from '../../components/ui/note/CategoryOnNote';
import TagInputOnNote from '../../components/ui/note/TagInputOnNote';
import BoardTypeSelectorOnNote from '../../components/ui/note/BoardTypeSelectorOnNote';
import BlankLine from "../../components/ui/note/BlankLine";
import NoteBlock from "../../components/ui/note/NoteBlock";
import AppLayout from "../../layout/AppLayout";
import {
    displayNameToCategoryMap,
    displayNameToBoardTypeMap,
    displayNameToDetailCategoryCodeMap,} from "../../constants/displayNameMaps";
import {DetailCategoryType} from "../../types/detailCategories";
import NoneLine from "../../components/ui/note/NoneLine";

interface Props {
    mode: 'create' | 'edit';
}

export default function BoardFormOnNotePage({ mode }: Props) {
    const { boardId } = useParams();
    const navigate = useNavigate();

    const [form, setForm] = useState<BoardDTO>({
        writer: 0,
        writerNickname: '',
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
                    writerNickname: '',
                    title: data.title,
                    content: data.content,
                    tags: data.tags,
                    category: displayNameToCategoryMap[data.category],
                    detailCategory: displayNameToDetailCategoryCodeMap[data.detailCategory],
                    boardType: displayNameToBoardTypeMap[data.boardType],
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
        <AppLayout>
            <div className="w-full rounded-t-lg shadow-lg">
                <div className="bg-blue-700 h-8 w-full rounded-t-lg shadow-md" />
                <NoteBlock className="flex justify-center py-6">
                    <h1 className="text-xl font-bold font-ui text-center">
                        {mode === 'create' ? '새 글 작성' : '글 수정'}
                    </h1>
                </NoteBlock>
                <BlankLine/>
                <InputOnNote
                    label="제목: "
                    value={form.title}
                    placeholder="제목을 입력해주세요."
                    onChange={(value) => setForm((prev) => ({ ...prev, title: value }))}
                />
                <BlankLine/>
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
                    maxRows={15}
                    minRows={15}
                />
                <TagInputOnNote
                    tags={form.tags ? form.tags.split(',') : []}
                    setTags={(tags) =>
                        setForm((prev) => ({ ...prev, tags: tags.join(',') }))
                    }
                />
                <NoneLine/>
                <NoteBlock className="justify-center gap-8">
                    <button
                        onClick={handleSubmit}
                        className="text-blue-800 font-semibold hover:underline"
                    >
                        {mode === 'create' ? '등록' : '수정'}
                    </button>
                    <button
                        onClick={() => navigate(`/boards`)}
                        className="text-black font-semibold hover:underline"
                    >
                        취소
                    </button>
                </NoteBlock>
            </div>
        </AppLayout>
    );
}
