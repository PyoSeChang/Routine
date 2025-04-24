import React from 'react';
import { RoutineViewDTO } from '../types/routine';

const UpcomingRoutineCard: React.FC<{ routine: RoutineViewDTO }> = ({ routine }) => (
    <div className="border border-gray-300 p-4 rounded-md shadow-sm bg-white w-[250px]">
        <h4 className="font-semibold text-lg">{routine.title}</h4>
        <ul className="mt-2 space-y-1 text-sm">
            {routine.tasks.map(task => (
                <li key={task.taskId}>{task.content}</li>
            ))}
        </ul>
    </div>
);

export default UpcomingRoutineCard;