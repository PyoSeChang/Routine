import React, { useState, useEffect } from "react";
import axios from "../../api/axios";
import CreateRoutineOverlay from "../../components/Routines/CreateRoutineOverlay";
import CategorySelector from "../../components/ui/CategorySelector";
import { RoutineSummaryDTO } from "../../types/routine";
import { Category } from "../../types/board";
import TagInput from "../../components/ui/TagInput";

export default function CircleFormPage() {
    const [circleName, setCircleName] = useState("");
    const [circleDescription, setCircleDescription] = useState("");
    const [category, setCategory] = useState<Category>(Category.LANGUAGE); // ✅ 초기값 LANGUAGE
    const [detailCategory, setDetailCategory] = useState("");
    const [tags, setTags] = useState<string[]>([]);
    const [routineOptions, setRoutineOptions] = useState<RoutineSummaryDTO[]>([]);
    const [createdRoutine, setCreatedRoutine] = useState<RoutineSummaryDTO | null>(null);
    const [selectedRoutineId, setSelectedRoutineId] = useState<number | null>(null);
    const memberId = localStorage.getItem("memberId");


    const [isCreateRoutineOpen, setIsCreateRoutineOpen] = useState(false);

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
            await axios.post("/circles", {
                circleName,
                circleDescription,
                category,
                detailCategory,
                tags: tags.join(","), // ✅ 태그 추가
                mode: "IMPORT",
                routineId: selectedRoutineId
            });
            // TODO: 성공 후 이동 처리
        } catch (error) {
            console.error("서클 생성 실패", error);
        }
    };

    const handleRoutineCreate = (routineId: number, title: string, category: Category, detailCategory: string) => {
        const newRoutine: RoutineSummaryDTO = { routineId, title, category, detailCategory };

        setRoutineOptions(prev => [...prev, newRoutine]); // ✅ 새 루틴을 루틴 목록에 추가
        setSelectedRoutineId(routineId); // ✅ 새 루틴 자동 선택
        setCategory(category); // ✅ category 고정
        setDetailCategory(detailCategory); // ✅ detailCategory 고정
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

            {/* 루틴 선택 */}
            <div className="mb-6">
                <select
                    value={selectedRoutineId ?? ""}
                    onChange={(e) => setSelectedRoutineId(Number(e.target.value))}
                    className="border p-2 rounded w-full"
                >
                    <option value="">루틴 선택</option>
                    {createdRoutine && (
                        <option value={createdRoutine.routineId} key={createdRoutine.routineId}>
                            {createdRoutine.title}
                        </option>
                    )}
                    {routineOptions.map(routine => (
                        <option key={routine.routineId} value={routine.routineId}>
                            {routine.title}
                        </option>
                    ))}
                </select>
            </div>

            {/* 루틴 새로 만들기 버튼 */}
            <div className="flex gap-4 mb-8">
                <button
                    className="bg-purple-500 text-white px-6 py-2 rounded hover:bg-purple-600"
                    onClick={() => setIsCreateRoutineOpen(true)}
                >
                    루틴 새로 만들기
                </button>
            </div>

            {/* 서클 생성 완료 버튼 */}
            <button
                className="bg-blue-500 text-white px-8 py-2 rounded hover:bg-blue-600"
                onClick={handleSubmit}
            >
                서클 생성 완료
            </button>

            {/* 루틴 만들기 오버레이 */}
            {isCreateRoutineOpen && (
                <CreateRoutineOverlay
                    onClose={() => setIsCreateRoutineOpen(false)}
                    onSave={(routineId, title) => {
                        // 루틴 새로 만들었을 때 → 바로 선택 + 카테고리 고정
                        handleRoutineCreate(routineId, title, category, detailCategory);
                    }}
                />
            )}
        </div>
    );
}
