import React, { useEffect } from 'react';
import { Weekday } from '../../types/routine';

type ViewMode = { type: 'DAY'; day: Weekday };

interface Props {
    viewMode: ViewMode;
    setViewMode: (mode: ViewMode) => void;
    className?: string;
}

const weekdays: Weekday[] = [
    'MONDAY', 'TUESDAY', 'WEDNESDAY',
    'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'
];

const WeekdayNav: React.FC<Props> = ({ viewMode, setViewMode }) => {
    useEffect(() => {
        // 오늘의 요일을 구해서 viewMode에 반영
        const today = new Date();
        const todayDay = weekdays[today.getDay() === 0 ? 6 : today.getDay() - 1]; // 일요일은 6으로 설정
        setViewMode({ type: 'DAY', day: todayDay });
    }, [setViewMode]);

    return (
        <div className="flex gap-2">
            {weekdays.map(day => (
                <button
                    key={day}
                    onClick={() => setViewMode({ type: 'DAY', day })}
                    className={`
                        font-chalk
                        text-white
                        text-[30px]
                        hover:underline
                        ${viewMode.day === day ? 'font-bold' : 'opacity-70'}
                    `}
                >
                    {`<${day}>`}
                </button>
            ))}
        </div>
    );
};

export default WeekdayNav;
