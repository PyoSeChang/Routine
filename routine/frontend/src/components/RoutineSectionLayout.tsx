import React from 'react';
import { RoutineViewDTO } from '../types/routine';

interface Props {
    circleRoutines: RoutineViewDTO[];
    personalRoutines: RoutineViewDTO[];
}

const RoutineSectionLayout: React.FC<Props> = ({ circleRoutines, personalRoutines }) => {
    return (
        <div className="flex gap-6 border border-black p-4 rounded-lg">

            {/* ✅ 서클 루틴 */}
            <div className="flex-1 border border-blue-300 rounded-lg p-4">
                <p className="font-bold text-xl mb-3">서클 루틴</p>
                <div className="flex gap-4 min-w-max">
                    {circleRoutines.map(routine => (
                        <div
                            key={routine.routineId}
                            className="min-w-[250px] max-w-xs border border-gray-300 rounded-lg p-4 bg-white shadow-sm"
                        >
                            <form>
                                <h5 className="text-lg font-semibold mb-2">{routine.title}</h5>
                                <ul className="list-disc list-inside text-sm text-gray-700">
                                    {routine.tasks.map(task => (
                                        <li key={task.taskId}>{task.content}</li>
                                    ))}
                                </ul>
                            </form>
                        </div>
                    ))}
                </div>
            </div>

            {/* ✅ 개인 루틴 */}
            <div className="flex-1 border border-green-300 rounded-lg p-4">
                <p className="font-bold text-xl mb-3">개인 루틴</p>
                <div className="flex gap-4 min-w-max">
                    {personalRoutines.map(routine => (
                        <div
                            key={routine.routineId}
                            className="min-w-[250px] max-w-xs border border-gray-300 rounded-lg p-4 bg-white shadow-sm"
                        >
                            <form>
                                <h5 className="text-lg font-semibold mb-2">{routine.title}</h5>
                                <ul className="list-disc list-inside text-sm text-gray-700">
                                    {routine.tasks.map(task => (
                                        <li key={task.taskId}>{task.content}</li>
                                    ))}
                                </ul>
                                <button
                                    type="button"
                                    className="mt-3 px-3 py-1 text-sm text-gray-700 border border-gray-400 rounded hover:bg-gray-100"
                                >
                                    수정
                                </button>
                            </form>
                        </div>
                    ))}
                </div>
            </div>

        </div>
    );
};

export default RoutineSectionLayout;
