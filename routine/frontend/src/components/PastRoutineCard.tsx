import React from 'react';
import { RoutineViewDTO } from '../types/routine';

const PastRoutineCard: React.FC<{ routine: RoutineViewDTO }> = ({ routine }) => (
    <div className="border border-gray-300 p-4 rounded-md shadow-sm bg-white w-[250px]">
        <h4 className="font-semibold text-lg">{routine.title}</h4>
        <ul className="mt-2 space-y-1 text-sm">
            {routine.tasks.map(task => (
                <li key={task.taskId} className="flex justify-between">
                    <span>{task.content}</span>
                    <span className="text-gray-500">{task.status}</span>
                </li>
            ))}
        </ul>
    </div>
);

export default PastRoutineCard;
