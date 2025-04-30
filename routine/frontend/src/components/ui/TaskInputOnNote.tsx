import React from 'react';
import Line from './Line';

interface TaskInputOnNoteProps {
    tasks: string[];
    setTasks: (tasks: string[]) => void;
}

const TaskInputOnNote: React.FC<TaskInputOnNoteProps> = ({ tasks, setTasks }) => {
    const handleChange = (index: number, value: string) => {
        const updated = [...tasks];
        updated[index] = value;
        setTasks(updated);
    };

    const handleRemove = (index: number) => {
        if (tasks.length <= 1) return;
        setTasks(tasks.filter((_, i) => i !== index));
    };

    const handleAdd = () => {
        if (tasks.length < 10) {
            setTasks([...tasks, '']);
        }
    };

    return (
        <div className="w-full">
            <Line className="font-ui">태스크</Line>

            {tasks.map((task, index) => (
                <Line key={index} className="gap-2">
                    <input
                        type="text"
                        value={task}
                        placeholder={`할 일 ${index + 1}`}
                        onChange={(e) => handleChange(index, e.target.value)}
                        className="w-full h-full bg-note font-user text-base  focus:outline-none"
                    />
                    <button
                        onClick={() => handleRemove(index)}
                        className="text-gray-500 hover:text-red-500"
                        disabled={tasks.length <= 1}
                    >
                        🗑️
                    </button>
                </Line>
            ))}

            {tasks.length < 10 && (
                <Line>
                    <button
                        onClick={handleAdd}
                        className="text-sm text-blue-600 hover:underline mt-2 px-1"
                    >
                        + 태스크 추가
                    </button>
                </Line>
            )}
        </div>
    );
};

export default TaskInputOnNote;
