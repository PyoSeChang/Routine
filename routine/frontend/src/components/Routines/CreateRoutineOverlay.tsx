import React, { useState, useEffect } from "react";
import axios from "../../api/axios";
import CategorySelector from "../../components/ui/CategorySelector";
import { Category } from "../../types/board"
import TagInput from "../ui/TagInput";

interface Props {
    onClose: () => void;
    onSave: (routineId: number, title: string) => void;
}

export default function CreateRoutineOverlay({ onClose, onSave }: Props) {
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [category, setCategory] = useState<Category>(Category.LANGUAGE);
    const [detailCategory, setDetailCategory] = useState<string>("");
    const [tags, setTags] = useState<string[]>([]);
    const [tasks, setTasks] = useState<string[]>([""]);
    const [repeatDays, setRepeatDays] = useState<string[]>([]);

    const handleSave = async () => {
        try {
            const res = await axios.post("/circles/create", {
                title,
                description,
                category,
                detailCategory,
                tags: tags.join(","),
                tasks,
                repeatDays,
            });

            const routineId = res.data.routineId;
            onSave(routineId, title);
        } catch (error) {
            console.error("루틴 생성 실패", error);
        }
    };

    useEffect(() => {
        const handleKeyDown = (e: KeyboardEvent) => {
            if (e.key === "Escape") {
                onClose();
            }
        };
        window.addEventListener("keydown", handleKeyDown);
        return () => {
            window.removeEventListener("keydown", handleKeyDown);
        };
    }, [onClose]);

    return (
        <div className="fixed inset-0 flex justify-center items-center z-50">
            <div className="bg-white p-6 rounded shadow-lg w-full max-w-lg relative">
                <button
                    className="absolute top-3 right-3 text-gray-500 hover:text-black"
                    onClick={onClose}
                >
                    ✕
                </button>

                <h2 className="text-xl font-bold mb-4">루틴 만들기</h2>

                <input
                    type="text"
                    placeholder="루틴 제목"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    className="border p-2 rounded w-full mb-4"
                />

                <textarea
                    placeholder="루틴 설명"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    className="border p-2 rounded w-full mb-4"
                    rows={4}
                />

                <div className="mb-4">
                    <CategorySelector
                        category={category}
                        detailCategory={detailCategory}
                        onCategoryChange={setCategory}
                        onDetailCategoryChange={setDetailCategory}
                    />
                </div>

                <TagInput tags={tags} setTags={setTags} />

                <div className="mb-4">
                    <label className="block font-medium mb-1">태스크</label>
                    {tasks.map((task, index) => (
                        <div key={index} className="max-h-60 overflow-y-auto flex gap-2 mb-2">
                            <input
                                type="text"
                                value={task}
                                onChange={(e) => {
                                    const updated = [...tasks];
                                    updated[index] = e.target.value;
                                    setTasks(updated);
                                }}
                                className="border p-2 rounded w-full"
                                placeholder={`할 일 ${index + 1}`}
                            />
                            <button
                                onClick={() => setTasks(tasks.filter((_, i) => i !== index))}
                                className="text-gray-500 hover:text-red-500"
                                disabled={tasks.length <= 1}
                            >
                                🗑️
                            </button>
                        </div>
                    ))}

                    {tasks.length < 10 && (
                        <button
                            onClick={() => setTasks([...tasks, ""])}
                            className="text-sm text-blue-600 hover:underline"
                        >
                            + 태스크 추가
                        </button>
                    )}
                </div>

                <div className="mb-4">
                    <label className="block font-medium mb-1">반복 요일</label>
                    <div className="flex flex-wrap gap-2">
                        {["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"].map(day => (
                            <button
                                key={day}
                                type="button"
                                onClick={() => {
                                    if (repeatDays.includes(day)) {
                                        setRepeatDays(repeatDays.filter(d => d !== day));
                                    } else {
                                        setRepeatDays([...repeatDays, day]);
                                    }
                                }}
                                className={`px-3 py-1 rounded-full border ${repeatDays.includes(day) ? "bg-blue-500 text-white" : "bg-gray-100 text-gray-700"}`}
                            >
                                {day.slice(0, 3)}
                            </button>
                        ))}
                    </div>
                </div>

                <button
                    className="bg-blue-500 text-white px-6 py-2 rounded w-full"
                    onClick={handleSave}
                >
                    저장
                </button>
            </div>
        </div>
    );
}
