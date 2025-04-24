import React from 'react';
import { Weekday } from '../types/routine';

type ViewMode = { type: 'DAY'; day: Weekday };

interface Props {
    viewMode: ViewMode;
    setViewMode: (mode: ViewMode) => void;
}

const weekdays: Weekday[] = [
    'MONDAY', 'TUESDAY', 'WEDNESDAY',
    'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'
];

const WeekdayNav: React.FC<Props> = ({ viewMode, setViewMode }) => {
    return (
        <div className="flex gap-2">
            {weekdays.map(day => (
                <button
                    key={day}
                    onClick={() => setViewMode({ type: 'DAY', day })}
                    className={`px-3 py-1 rounded border ${viewMode.day === day ? 'bg-blue-500 text-white' : 'bg-white text-gray-700'}`}
                >
                    {day}
                </button>
            ))}
        </div>
    );
};

export default WeekdayNav;
