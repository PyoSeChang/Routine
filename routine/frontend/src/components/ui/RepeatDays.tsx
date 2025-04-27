import React from 'react';
import { Weekday } from '../../types/routine';

interface RepeatDaysProps {
    selectedDays?: Weekday[];
    onChange?: (day: Weekday) => void;
    disabled?: boolean;
}

const weekdayIcons: Record<Weekday, string> = {
    MONDAY: '/icons/monday.png',
    TUESDAY: '/icons/tuesday.png',
    WEDNESDAY: '/icons/wednesday.png',
    THURSDAY: '/icons/thursday.png',
    FRIDAY: '/icons/friday.png',
    SATURDAY: '/icons/saturday.png',
    SUNDAY: '/icons/sunday.png',
};

const RepeatDays: React.FC<RepeatDaysProps> = ({ selectedDays = [], onChange, disabled = false }) => {
    return (
        <div className="flex gap-2">
            {selectedDays.map((day) => (
                <label key={day} className="cursor-pointer">
                    {onChange && (
                        <input
                            type="checkbox"
                            className="hidden"
                            checked
                            disabled={disabled}
                            onChange={() => onChange(day)}
                        />
                    )}
                    <div className="w-10 h-10 flex items-center justify-center">
                        <img
                            src={weekdayIcons[day]}
                            alt={day}
                            className="w-8 h-8 object-contain"
                        />
                    </div>
                </label>
            ))}
        </div>
    );
};

export default RepeatDays;
