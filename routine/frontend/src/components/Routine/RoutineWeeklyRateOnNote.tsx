// 2. Task Weekly Rate
import { DailyRateDTO } from "../../types/routineRates";
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

interface WeeklyRateProps {
    thisWeekRates: DailyRateDTO[];
    lastWeekRates: DailyRateDTO[];
}

const RoutineTaskWeeklyRateOnNote: React.FC<WeeklyRateProps> = ({
                                                                    thisWeekRates,
                                                                    lastWeekRates,
                                                                }) => {
    const [currentWeek, setCurrentWeek] = useState<'this' | 'last'>('this');
    const handlePrev = () => setCurrentWeek('last');
    const handleNext = () => setCurrentWeek('this');

    const data = currentWeek === 'this' ? thisWeekRates : lastWeekRates;

    return (
        <div className="w-full">
            <NoteBlock className="pl-20">
                <div className="max-w-[600px] w-full h-[330px] mt-5 shadow-lg bg-postit-green p-4 flex flex-col justify-between">
                    <div className="flex justify-between items-center mb-2">
                        <div className="text-sm font-semibold text-gray-700">요일별 이행률</div>
                        <div className="flex items-center gap-2">
                            {currentWeek === 'this' && (
                                <button onClick={handlePrev} className="hover:text-blue-600">
                                    <ChevronLeft className="w-5 h-5" />
                                </button>
                            )}
                            <span className="text-sm font-semibold text-gray-800">
                                {currentWeek === 'this' ? '이번 주 이행률' : '저번 주 이행률'}
                            </span>
                            {currentWeek === 'last' && (
                                <button onClick={handleNext} className="hover:text-blue-600">
                                    <ChevronRight className="w-5 h-5" />
                                </button>
                            )}
                        </div>
                    </div>

                    <ResponsiveContainer width="100%" height="100%">
                        <LineChart data={data}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="date" />
                            <YAxis
                                domain={[0, 1]}
                                tickFormatter={(v) => `${(v * 100).toFixed(0)}%`}
                            />
                            <Tooltip formatter={(v: number) => `${(v * 100).toFixed(0)}%`} />
                            <ReLine
                                type="monotone"
                                dataKey="commitRate"
                                stroke="#82ca9d"
                            />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            </NoteBlock>
        </div>
    );
};

export default RoutineTaskWeeklyRateOnNote;
