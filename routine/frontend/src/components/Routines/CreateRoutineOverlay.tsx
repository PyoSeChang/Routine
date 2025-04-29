import React, { useEffect, useState } from 'react';
import { Weekday } from '../../types/routine';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { CategorySelector } from '../../components/ui/CategorySelector';
import { Category } from '../../types/board';

interface Props {
    onClose: () => void;
    isCircleRoutine?: boolean;
    circleId?: number;
}

const WEEKDAYS: Weekday[] = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

const CreateRoutineOverlay: React.FC<Props> = ({ onClose, isCircleRoutine = false, circleId }) => {
    const [form, setForm] = useState({
        title: '',
        category: Category.LANGUAGE,
        detailCategory: '',
        tags: '',
        description: '',
        repeatDays: [] as Weekday[],
        tasks: ['', '', ''],
        circleRoutine: isCircleRoutine,
        circleId: circleId ?? null,
    });

    const navigate = useNavigate();

    const handleInputChange = (field: keyof typeof form, value: any) => {
        setForm(prev => ({ ...prev, [field]: value }));
    };

    const handleDayToggle = (day: Weekday) => {
        setForm(prev => ({
            ...prev,
            repeatDays: prev.repeatDays.includes(day)
                ? prev.repeatDays.filter(d => d !== day)
                : [...prev.repeatDays, day],
        }));
    };

    const handleTaskChange = (index: number, value: string) => {
        const updatedTasks = [...form.tasks];
        updatedTasks[index] = value;
        setForm(prev => ({ ...prev, tasks: updatedTasks }));
    };

    const handleAddTask = () => {
        if (form.tasks.length < 10) {
            setForm(prev => ({ ...prev, tasks: [...prev.tasks, ''] }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const payload = {
            ...form,
            tasks: form.tasks.filter(t => t.trim() !== ''),
        };

        console.log('보내는 payload:', payload);

        try {
            await axios.post('/api/routine/new-routine', payload, {
                headers: { 'Content-Type': 'application/json' },
            });
            alert('루틴이 성공적으로 생성되었습니다!');
            window.location.reload();
        } catch (error: any) {
            if (error.response) {
                console.error('서버 에러 상태:', error.response.status);
                console.error('서버 에러 내용:', error.response.data);
            } else {
                console.error('루틴 생성 실패:', error);
            }
            alert('루틴 생성에 실패했습니다.');
        }
    };

    useEffect(() => {
        const handleEsc = (e: KeyboardEvent) => {
            if (e.key === 'Escape') onClose();
        };
        window.addEventListener('keydown', handleEsc);
        return () => window.removeEventListener('keydown', handleEsc);
    }, [onClose]);

    return (
        <div className="fixed inset-0 flex items-center justify-center z-50">
            <div className="bg-white p-8 rounded-lg w-[450px] shadow-lg relative">
                <button className="absolute top-2 right-2 text-gray-500 hover:text-gray-700" onClick={onClose}>✕</button>
                <h2 className="text-2xl font-bold mb-6 text-center">루틴 만들기</h2>
                <form className="space-y-4" onSubmit={handleSubmit}>

                    <input
                        type="text"
                        placeholder="루틴 이름"
                        className="w-full border rounded-lg p-2"
                        value={form.title}
                        onChange={e => handleInputChange('title', e.target.value)}
                        required
                    />

                    <div className="flex flex-wrap gap-2">
                        {WEEKDAYS.map(day => (
                            <label key={day} className="flex items-center space-x-1">
                                <input
                                    type="checkbox"
                                    checked={form.repeatDays.includes(day)}
                                    onChange={() => handleDayToggle(day)}
                                />
                                <span>{day.slice(0, 3)}</span>
                            </label>
                        ))}
                    </div>

                    <CategorySelector
                        category={form.category}
                        detailCategory={form.detailCategory}
                        onCategoryChange={(newCategory) => setForm(prev => ({ ...prev, category: newCategory, detailCategory: '' }))}
                        onDetailCategoryChange={(newDetailCategory) => setForm(prev => ({ ...prev, detailCategory: newDetailCategory }))}
                    />

                    <input
                        type="text"
                        placeholder="태그 입력 (쉼표로 구분)"
                        className="w-full border rounded-lg p-2"
                        value={form.tags}
                        onChange={e => handleInputChange('tags', e.target.value)}
                    />

                    <textarea
                        placeholder="루틴 설명 작성"
                        className="w-full border rounded-lg p-2"
                        rows={3}
                        value={form.description}
                        onChange={e => handleInputChange('description', e.target.value)}
                    />

                    <div className="space-y-2 max-h-[300px] overflow-y-auto">
                        {form.tasks.map((task, index) => (
                            <input
                                key={index}
                                type="text"
                                placeholder={`Task ${index + 1}`}
                                className="w-full border rounded-lg p-2"
                                value={task}
                                onChange={e => handleTaskChange(index, e.target.value)}
                            />
                        ))}
                        {form.tasks.length < 10 && (
                            <button
                                type="button"
                                onClick={handleAddTask}
                                className="mt-2 w-full border rounded-lg p-2 text-blue-500 hover:bg-blue-100"
                            >
                                + 태스크 추가
                            </button>
                        )}
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-blue-500 text-white rounded-lg p-2 hover:bg-blue-600 mt-4"
                    >
                        저장하기
                    </button>
                </form>
            </div>
        </div>
    );
};

export default CreateRoutineOverlay;