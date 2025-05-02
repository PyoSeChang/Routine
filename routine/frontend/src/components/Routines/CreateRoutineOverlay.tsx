import React, { useState, useEffect } from "react";
import axios from "../../api/axios";
import { Category } from "../../types/board";
import { Weekday } from '../../types/routine';

import InputOnNote from "../ui/note/InputOnNote";
import BlankLine from "../ui/note/BlankLine";
import CategoryOnNote from "../ui/note/CategoryOnNote";
import TagInputOnNote from "../ui/note/TagInputOnNote";
import TaskInputOnNote from "../ui/note/TaskInputOnNote";
import RepeatDaysOnNote from "../ui/note/RepeatDaysOnNote";
import TextareaOnNote from "../ui/note/TextareaOnNote";
import NoneLine from "../ui/note/NoneLine";

interface Props {
    onClose: () => void;
    onSave: (routineId: number, title: string) => void;
}

export default function CreateRoutineOverlay({ onClose, onSave }: Props) {
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [category, setCategory] = useState<Category>(Category.NONE);
    const [detailCategory, setDetailCategory] = useState<string>("");
    const [tags, setTags] = useState<string[]>([]);
    const [tasks, setTasks] = useState<string[]>([""]);
    const [repeatDays, setRepeatDays] = useState<Weekday[]>([]);

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
            <div className="relative w-full max-w-lg before:content-['']
            before:absolute before:left-10 before:top-0 before:bottom-0 before:z-[10]
            before:w-[2px] before:bg-red-500 before:opacity-60">
                <div className="bg-blue-700 h-6 w-full rounded-t-md shadow-md" />
                <div className="relative bg-note shadow-lg shawdow-black/30 w-full pt-3">

                    <button
                        className="absolute top-3 right-3 text-gray-500 hover:text-black"
                        onClick={onClose}
                    >
                        ✕
                    </button>

                    <h2 className="text-xl font-bold mb-4 pt-2 text-center font-ui">루틴 만들기</h2>

                    <BlankLine/>
                    <InputOnNote label="이름: " value={title} onChange={setTitle} />
                    <BlankLine count={1} />
                    <CategoryOnNote category={category} detailCategory={detailCategory} onCategoryChange={setCategory} onDetailCategoryChange={setDetailCategory} />
                    <BlankLine count={1} />
                    <TagInputOnNote tags={tags} setTags={setTags} />
                    <BlankLine count={1} />
                    <TaskInputOnNote tasks={tasks} setTasks={setTasks} />
                    <BlankLine count={1} />
                    <RepeatDaysOnNote
                        selectedDays={repeatDays}
                        onChange={(day) =>
                            setRepeatDays(prev =>
                                prev.includes(day) ? prev.filter(d => d !== day) : [...prev, day]
                            )
                        }
                    />
                    <TextareaOnNote
                        value={description}
                        onChange={setDescription}
                        placeholder="루틴 설명을 간단히 적어주세요"
                        maxRows={3}
                    />
                    <NoneLine />

                    <button
                        className="bg-note px-6 py-2 w-full"
                        onClick={handleSave}
                    >
                        저장
                    </button>
                </div>
            </div>
        </div>
    );
}