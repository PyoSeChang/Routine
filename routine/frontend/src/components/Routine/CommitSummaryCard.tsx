import React from 'react';
import Line from "../ui/note/Line";
import TaskCheckBox from "../ui/TaskCheckBox";

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
        <div className="w-full">
            <Line className="font-bold text-lg text-center">{title}</Line>
            {tasks.map((task) => (
                <Line key={task.taskId} className="items-center gap-2">
                    {task.content && (
                        <TaskCheckBox
                            label={task.content}
                            checked={task.status === 'SUCCESS'}
                            disabled
                            onChange={() => {}}
                        />
                    )}
                </Line>
            ))}
        </div>
    );
}
