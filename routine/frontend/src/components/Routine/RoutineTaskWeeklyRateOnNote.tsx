// 2. Task Weekly Rate
import { TaskWeeklyRateDTO, WeeklyRateDTO } from "../../types/routineRates";
import React, { useMemo, useState } from "react";
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
import BlankLine from "../ui/note/BlankLine";
import dayjs from "dayjs";
import isoWeek from "dayjs/plugin/isoWeek";

dayjs.extend(isoWeek);

interface TaskRateProps {
    routineWeeklyRates: WeeklyRateDTO[];
    taskWeeklyRates: TaskWeeklyRateDTO[];
}

function formatWeekLabel(weekStr: string): string {
    const match = weekStr.match(/^(\d{4})-W(\d{1,2})$/);
    if (!match) return weekStr;

    const [, yearStr, weekStrNum] = match;
    const year = parseInt(yearStr, 10);
    const week = parseInt(weekStrNum, 10);

    const date = dayjs().year(year).isoWeek(week).startOf("isoWeek");
    const month = date.month() + 1;
    const weekOfMonth = Math.ceil((date.date() + date.day()) / 7);

    return `${month}월 ${weekOfMonth}주차`;
}

const COLOR_PALETTE = [
    "#e6194b", "#3cb44b", "#911eb4", "#4363d8", "#f58231",
    "#ffe119", "#46f0f0", "#f032e6", "#bcf60c", "#fabebe"
];

const RoutineTaskWeeklyRateOnNote: React.FC<TaskRateProps> = ({
                                                                  routineWeeklyRates,
                                                                  taskWeeklyRates,
                                                              }) => {
    const [selectedTaskIds, setSelectedTaskIds] = useState<Set<number>>(new Set());

    const toggleTask = (taskId: number) => {
        setSelectedTaskIds((prev) => {
            const next = new Set(prev);
            if (next.has(taskId)) next.delete(taskId);
            else next.add(taskId);
            return next;
        });
    };

    const colorMap = useMemo(() => {
        const map: Record<number, string> = {};
        taskWeeklyRates.forEach((task, idx) => {
            map[task.taskId] = COLOR_PALETTE[idx % COLOR_PALETTE.length];
        });
        return map;
    }, [taskWeeklyRates]);

    const weekLabels = Array.from(
        new Set([
            ...routineWeeklyRates.map((r) => r.week),
            ...taskWeeklyRates.flatMap((t) => t.rates.map((r) => r.week)),
        ])
    ).sort();

    const mergedData = weekLabels.map((week) => {
        const entry: Record<string, any> = { week };

        const routine = routineWeeklyRates.find((r) => r.week === week);
        if (routine) entry.commitRate = routine.commitRate;

        for (const task of taskWeeklyRates) {
            const rate = task.rates.find((r) => r.week === week);
            if (rate) entry[task.taskContent] = rate.commitRate;
        }

        return entry;
    });

    return (
        <div className="w-full ">
            <NoteBlock className="pl-20">
                <div className="w-full">
                    <div className="flex flex-col gap-1 mb-2">
                        {taskWeeklyRates.map((task) => (
                            <label
                                key={task.taskId}
                                className="flex items-center gap-1 text-sm"
                                style={{ color: colorMap[task.taskId] }}
                            >
                                <input
                                    type="checkbox"
                                    checked={selectedTaskIds.has(task.taskId)}
                                    onChange={() => toggleTask(task.taskId)}
                                />
                                {task.taskContent}
                            </label>
                        ))}
                    </div>

                    <div className="max-w-[600px] w-full flex-1 h-[330px] shadow-lg bg-postit-green p-4 flex flex-col justify-between">
                        <div className="text-sm font-semibold text-gray-700 mb-1">
                            루틴 전체 평균 + 선택된 태스크들의 주차별 이행률
                        </div>
                        <ResponsiveContainer width="100%" height="100%">
                            <LineChart data={mergedData}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="week" tickFormatter={formatWeekLabel} />
                                <YAxis
                                    domain={[0, 1]}
                                    tickFormatter={(v: number) => `${(v * 100).toFixed(0)}%`}
                                />
                                <Tooltip
                                    labelFormatter={(label) => formatWeekLabel(label)}
                                    formatter={(v: number) => `${(v * 100).toFixed(0)}%`}
                                />
                                <ReLine
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
                                            type="monotone"
                                            dataKey={task.taskContent}
                                            stroke={colorMap[task.taskId]}
                                            name={task.taskContent}
                                        />
                                    ))}
                            </LineChart>
                        </ResponsiveContainer>
                    </div>
                </div>
            </NoteBlock>
            <BlankLine />
        </div>
    );
};

export default RoutineTaskWeeklyRateOnNote;
