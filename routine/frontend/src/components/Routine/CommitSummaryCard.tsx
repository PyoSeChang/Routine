import React from 'react';

interface TaskDTO {
    taskId: number;
    content: string;
    status: string;
    checked: boolean;
}

interface CommitSummaryCardProps {
    title: string;
    tasks: TaskDTO[];
}

export default function CommitSummaryCard({ title, tasks }: CommitSummaryCardProps) {
    return (
        <div className="border border-gray-300 p-4 rounded-md shadow-sm bg-white w-[250px]">
            <h4 className="font-bold text-lg mb-3">{title}</h4>
            <ul className="space-y-2">
                {tasks.map((task) => (
                    <li key={task.taskId} className="flex items-center gap-2">
                        <input
                            type="checkbox"
                            checked={task.status === 'SUCCESS'}
                            disabled
                            className="form-checkbox"
                        />
                        <span className={task.status === 'SUCCESS' ? 'line-through text-blue-400' : ''}>
              {task.content}
            </span>
                    </li>
                ))}
            </ul>
        </div>
    );
}
