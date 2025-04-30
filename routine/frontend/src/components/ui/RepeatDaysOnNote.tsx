import React from 'react';
import { Weekday } from '../../types/routine';
import Line from './Line';

interface RepeatDaysOnNoteProps {
    selectedDays: Weekday[];
    onChange?: (day: Weekday) => void;
    disabled?: boolean;
}

const weekdays: Weekday[] = [
    'MONDAY',
    'TUESDAY',
    'WEDNESDAY',
    'THURSDAY',
    'FRIDAY',
    'SATURDAY',
    'SUNDAY',
];

const weekdayIcons: Record<Weekday, string> = {
    MONDAY: '/icons/monday.png',
    TUESDAY: '/icons/tuesday.png',
    WEDNESDAY: '/icons/wednesday.png',
    THURSDAY: '/icons/thursday.png',
    FRIDAY: '/icons/friday.png',
    SATURDAY: '/icons/saturday.png',
    SUNDAY: '/icons/sunday.png',
};

const RepeatDaysOnNote: React.FC<RepeatDaysOnNoteProps> = ({
                                                               selectedDays,
                                                               onChange,
                                                               disabled = false,
                                                           }) => {
    const handleToggle = (day: Weekday) => {
        if (!disabled && onChange) {
            onChange(day);
        }
    };

    return (
        <div className="w-full">
            {/* 라벨 */}
            <Line className="font-ui">반복 요일</Line>

            {/* 요일 아이콘들 */}
            <Line className="justify-evenly px-0">
                {weekdays.map((day) => {
                    const isChecked = selectedDays.includes(day);

                    return (
                        <button
                            key={day}
                            type="button"
                            onClick={() => handleToggle(day)}
                            className={`relative w-10 h-10 flex-shrink-0 ${
                                disabled ? 'cursor-default' : 'cursor-pointer'
                            }`}
                        >
                            <img
                                src={weekdayIcons[day]}
                                alt={day}
                                className={`relative w-8 h-8 flex-shrink-0 ${disabled ? 'cursor-default' : 'cursor-pointer'}`}
                            />
                            {isChecked && (
                                <img
                                    src="/icons/check.png"
                                    alt="checked"
                                    className="absolute inset-0 w-6 h-6 object-contain pointer-events-none"
                                    style={{ top: '3px', left: '6px', position: 'absolute' }}
                                />
                            )}
                        </button>
                    );
                })}
            </Line>
        </div>
    );
};

export default RepeatDaysOnNote;
