import React, { useState, useEffect } from "react";
import axios from "../../api/axios";
import CreateRoutineOverlay from "../../components/Routines/CreateRoutineOverlay";
import CategorySelector from "../../components/ui/CategorySelector";
import { RoutineSummaryDTO } from "../../types/routine";
import { Category } from "../../types/board";
import TagInput from "../../components/ui/TagInput";
import { useNavigate } from 'react-router-dom';

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
        if (!routineId || !title) {
            console.error("handleRoutineCreate: invalid routineId or title", { routineId, title });
            return;
        }

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

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">서클 만들기</h1>

            <input
                type="text"
                placeholder="서클 이름"
                value={circleName}
                onChange={(e) => setCircleName(e.target.value)}
                className="border p-2 rounded w-full mb-4"
            />

            <textarea
                placeholder="서클 설명"
                value={circleDescription}
                onChange={(e) => setCircleDescription(e.target.value)}
                className="border p-2 rounded w-full mb-6"
            />

            {/* 카테고리 선택 */}
            <div className="mb-6">
                <CategorySelector
                    category={category}
                    detailCategory={detailCategory}
                    onCategoryChange={setCategory}
                    onDetailCategoryChange={setDetailCategory}
                />
            </div>

            <TagInput tags={tags} setTags={setTags} />

            {/* ✅ 공개 여부 */}
            {/* ✅ 공개 여부 */}
            <div className="mb-6">
                <label className="mr-4 font-semibold">공개 여부:</label>
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
            </div>


            {/* ✅ 최대 멤버 수 */}
            <div className="mb-6">
                <label className="block font-semibold mb-1">최대 멤버 수:</label>
                <input
                    type="number"
                    min={2}
                    value={maxMemberCount}
                    onChange={(e) => setMaxMemberCount(parseInt(e.target.value))}
                    className="border p-2 rounded w-full"
                />
            </div>

            {/* 루틴 선택 */}
            <div className="mb-6">
                <select
                    value={selectedRoutineId != null ? selectedRoutineId.toString() : ""}
                    onChange={(e) => setSelectedRoutineId(Number(e.target.value))}
                    className="border p-2 rounded w-full"
                >
                    <option value="">루틴 선택</option>
                    {routineOptions.map((routine, idx) => {
                        if (!routine?.routineId) return null;
                        return (
                            <option key={routine.routineId} value={routine.routineId.toString()}>
                                {routine.title}
                            </option>
                        );
                    })}
                </select>
            </div>

            {/* 루틴 새로 만들기 */}
            <div className="flex gap-4 mb-8">
                <button
                    className="bg-purple-500 text-white px-6 py-2 rounded hover:bg-purple-600"
                    onClick={() => setIsCreateRoutineOpen(true)}
                >
                    루틴 새로 만들기
                </button>
            </div>

            <button
                className="bg-blue-500 text-white px-8 py-2 rounded hover:bg-blue-600"
                onClick={handleSubmit}
            >
                서클 생성 완료
            </button>

            {isCreateRoutineOpen && (
                <CreateRoutineOverlay
                    onClose={() => setIsCreateRoutineOpen(false)}
                    onSave={(routineId, title) => {
                        handleRoutineCreate(routineId, title, category, detailCategory);
                    }}
                />
            )}
        </div>
    );
}
