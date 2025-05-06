// 2. Task Weekly Rate
import { TaskWeeklyRateDTO, WeeklyRateDTO } from "../../types/routineRates";
import React, { useState } from "react";
import NoteLine from "../ui/note/Line";
import {
    CartesianGrid,
    LineChart,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis,
    Line as ReLine,
} from "recharts";
import NoteBlock from "../ui/note/NoteBlock";
import NoneLine from "../ui/note/NoneLine";
import { ChevronLeft, ChevronRight } from 'lucide-react';
import BlankLine from "../ui/note/BlankLine";

interface TaskRateProps {
    routineWeeklyRates: WeeklyRateDTO[];
    taskWeeklyRates: TaskWeeklyRateDTO[];
}

const RoutineTaskWeeklyRateOnNote: React.FC<TaskRateProps> = ({
                                                                  routineWeeklyRates,
                                                                  taskWeeklyRates,
                                                              }) => {
    const [selectedTaskIds, setSelectedTaskIds] = useState<Set<number>>(new Set());
    const [currentWeek, setCurrentWeek] = useState<'this' | 'last'>('this');
    const handlePrev = () => setCurrentWeek('last');
    const handleNext = () => setCurrentWeek('this');

    const toggleTask = (taskId: number) => {
        setSelectedTaskIds((prev) => {
            const next = new Set(prev);
            if (next.has(taskId)) next.delete(taskId);
            else next.add(taskId);
            return next;
        });
    };

    return (
        <div className="w-full ">


            <NoteLine className="flex-wrap gap-2">
                {taskWeeklyRates.map((task) => (
                    <label key={task.taskId} className="flex items-center gap-1 text-sm">
                        <input
                            type="checkbox"
                            checked={selectedTaskIds.has(task.taskId)}
                            onChange={() => toggleTask(task.taskId)}
                        />
                        {task.taskContent}
                    </label>
                ))}
            </NoteLine>

            <NoteBlock className="pl-20">
                <div className="max-w-[600px] w-full h-[330px] mt-5 shadow-lg bg-postit-green p-4 flex flex-col justify-between">
                    <div className="text-sm font-semibold text-gray-700 mb-1">루틴 전체 평균 + 선택된 태스크들의 주차별 이행률</div>
                    <ResponsiveContainer width="100%" height="100%">
                        <LineChart>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="week" />
                            <YAxis
                                domain={[0, 1]}
                                tickFormatter={(v: number) => `${(v * 100).toFixed(0)}%`}
                            />
                            <Tooltip formatter={(v: number) => `${(v * 100).toFixed(0)}%`} />
                            <ReLine
                                data={routineWeeklyRates}
                                type="monotone"
                                dataKey="commitRate"
                                stroke="#8884d8"
                                name="루틴 평균"
                            />
                            {taskWeeklyRates
                                .filter((t) => selectedTaskIds.has(t.taskId))
                                .map((task) => (
                                    <ReLine
                                        key={task.taskId}
                                        data={task.rates}
                                        type="monotone"
                                        dataKey="commitRate"
                                        stroke="#ff7300"
                                        name={task.taskContent}
                                    />
                                ))}
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            </NoteBlock>
            <BlankLine/>
        </div>
    );
};

export default RoutineTaskWeeklyRateOnNote;