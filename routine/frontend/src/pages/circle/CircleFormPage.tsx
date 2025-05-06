import React, { useState, useEffect } from "react";
import axios from "../../api/axios";
import CreateRoutineOverlay from "../../components/Routines/CreateRoutineOverlay";
import { RoutineSummaryDTO } from "../../types/routine";
import { Category } from "../../types/board";
import { useNavigate } from 'react-router-dom';
import AppLayout from "../../layout/AppLayout";
import InputOnNote from "../../components/ui/note/InputOnNote";
import TextareaOnNote from "../../components/ui/note/TextareaOnNote";
import CategoryOnNote from "../../components/ui/note/CategoryOnNote";
import TagInputOnNote from "../../components/ui/note/TagInputOnNote";
import NoteBlock from "../../components/ui/note/NoteBlock";
import Line from "../../components/ui/note/Line";
import DropdownOnNote from "../../components/ui/note/DropdownOnNote";
import NoneLine from "../../components/ui/note/NoneLine";

export default function CircleFormPage() {
    const [circleName, setCircleName] = useState("");
    const [circleDescription, setCircleDescription] = useState("");
    const [category, setCategory] = useState<Category>(Category.LANGUAGE);
    const [detailCategory, setDetailCategory] = useState("");
    const [tags, setTags] = useState<string[]>([]);
    const [routineOptions, setRoutineOptions] = useState<RoutineSummaryDTO[]>([]);
    const [selectedRoutineId, setSelectedRoutineId] = useState<number | null>(null);
    const [isOpened, setIsOpened] = useState(true);
    const [maxMemberCount, setMaxMemberCount] = useState(10);
    const [isCreateRoutineOpen, setIsCreateRoutineOpen] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (category && detailCategory) {
            const fetchRoutines = async () => {
                try {
                    const res = await axios.get("/circles/my-routines", {
                        params: { category, detailCategory }
                    });
                    setRoutineOptions(res.data);
                } catch (error) {
                    console.error("루틴 불러오기 실패", error);
                    setRoutineOptions([]);
                }
            };
            fetchRoutines();
        } else {
            setRoutineOptions([]);
        }
    }, [category, detailCategory]);

    const handleSubmit = async () => {
        if (!selectedRoutineId) {
            alert("루틴을 선택하거나 만들어야 합니다.");
            return;
        }

        try {
            const res = await axios.post("/circles", {
                circleName,
                circleDescription,
                category,
                detailCategory,
                tags: tags.join(","),
                routineId: selectedRoutineId,
                mode: "IMPORT",
                opened: isOpened,
                maxMemberCount,
            });

            const circleId = res.data.circleId;
            navigate(`/circles/${circleId}`);

        } catch (error) {
            console.error("서클 생성 실패", error);
        }
    };

    const handleRoutineCreate = (
        routineId: number,
        title: string,
        category: Category,
        detailCategory: string
    ) => {
        if (!routineId || !title) return;

        const newRoutine: RoutineSummaryDTO = {
            routineId,
            title,
            category,
            detailCategory,
        };

        setRoutineOptions((prev) => [...prev, newRoutine]);
        setSelectedRoutineId(routineId);
        setCategory(category);
        setDetailCategory(detailCategory);
        setIsCreateRoutineOpen(false);
    };

    const routineDropdownOptions = routineOptions.map((routine) => ({
        value: routine.routineId.toString(),
        label: routine.title
    }));

    return (
        <AppLayout>
            <div className="p-6 w-full max-w-[800px] ">
                <div className="bg-blue-700 h-6 w-full rounded-t-md shadow-lg " />
                <NoteBlock className="justify-center pt-5 pb-1">
                    <h1 className="text-2xl font-bold mb-6">서클 개설 신청서</h1>
                </NoteBlock>
                <InputOnNote
                    type="text"
                    placeholder="서클 이름"
                    value={circleName}
                    label="서클 이름: "
                    onChange={setCircleName}
                />

                <TextareaOnNote
                    value={circleDescription}
                    onChange={setCircleDescription}
                    label="서클 설명: "
                    minRows={3}
                    maxRows={3}
                />

                <CategoryOnNote
                    category={category}
                    detailCategory={detailCategory}
                    onCategoryChange={setCategory}
                    onDetailCategoryChange={setDetailCategory}
                />

                <TagInputOnNote tags={tags} setTags={setTags} />

                <InputOnNote
                    indent={'pl-[70px]'}
                    label="최대 인원:"
                    type="number"
                    value={maxMemberCount.toString()}
                    onChange={(val) => setMaxMemberCount(Number(val))}
                />

                <NoteBlock>
                    <Line>
                        <label className="mr-4 font-ui">공개 여부:</label>
                        <label className="mr-4">
                            <input
                                type="radio"
                                name="isOpened"
                                value="true"
                                checked={isOpened}
                                onChange={() => setIsOpened(true)}
                            /> 공개
                        </label>
                        <label>
                            <input
                                type="radio"
                                name="isOpened"
                                value="false"
                                checked={!isOpened}
                                onChange={() => setIsOpened(false)}
                            /> 비공개
                        </label>
                    </Line>
                </NoteBlock>

                <DropdownOnNote
                    value={selectedRoutineId?.toString() || ""}
                    options={routineDropdownOptions}
                    onChange={(val) => setSelectedRoutineId(Number(val))}
                    label="루틴 선택: "
                />
                <NoteBlock >
                    <Line className="justify-center"
                        indent={false}>
                        <button
                            onClick={() => setIsCreateRoutineOpen(true)}
                            className="mx-auto font-ui text-[20px] font-bold text-black hover:underline"
                        >
                            &gt;&gt; 루틴 새로 만들기 &lt;&lt;
                        </button>
                    </Line>
                </NoteBlock>
                <NoneLine/>
                <NoteBlock className="justify-center">
                    <button
                        className="  text-black px-8 py-2 rounded hover:underline"
                        onClick={handleSubmit}
                    >
                        제출하기
                    </button>
                </NoteBlock>


                {isCreateRoutineOpen && (
                    <CreateRoutineOverlay
                        onClose={() => setIsCreateRoutineOpen(false)}
                        onSave={(routineId, title) => {
                            handleRoutineCreate(routineId, title, category, detailCategory);
                        }}
                    />
                )}
            </div>
        </AppLayout>
    );
}
