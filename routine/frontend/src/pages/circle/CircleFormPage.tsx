import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createCircle } from '../../hooks/useCircle';
import { CircleDTO } from '../../types/circle';
import { CategorySelector } from '../../components/ui/CategorySelector';
import { Category } from '../../types/board';
import { useCurrentUser } from '../../hooks/useAuth';

export default function CircleFormPage() {
    const navigate = useNavigate();
    const currentUser = useCurrentUser();

    const [form, setForm] = useState<CircleDTO>({
        name: '',
        description: '',
        tags: '',
        isPublic: true,
        category: Category.LANGUAGE,
        detailCategory: '',
        leaderId: 0,
    });

    useEffect(() => {
        if (currentUser) {
            setForm(prev => ({
                ...prev,
                leaderId: currentUser.memberId,
            }));
        }
    }, [currentUser]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value, type } = e.target;
        const checked = (e.target as HTMLInputElement).checked;
        setForm(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value,
        }));
    };

    const handleCategoryChange = (newCategory: Category) => {
        setForm(prev => ({
            ...prev,
            category: newCategory,
            detailCategory: '',
        }));
    };

    const handleDetailCategoryChange = (newDetailCategory: string) => {
        setForm(prev => ({
            ...prev,
            detailCategory: newDetailCategory,
        }));
    };

    const handleSubmit = async () => {
        console.log('📦 [handleSubmit] 최종 form 데이터:', JSON.stringify(form, null, 2));

        try {
            console.log('🚀 [createCircle] 요청 보냄: /api/circles');
            const response = await createCircle(form);

            console.log('✅ [createCircle] 응답 성공:', response);
            // navigate('/circles');
        } catch (err: any) {
            console.error('🛑 [createCircle] 요청 실패');
            if (err.response) {
                console.error('Status:', err.response.status);
                console.error('Data:', JSON.stringify(err.response.data, null, 2));
                console.error('Headers:', err.response.headers);
                console.error('URL:', err.response.config.url);
                console.error('Method:', err.response.config.method);
                console.error('Request Data:', JSON.stringify(err.response.config.data, null, 2));
            } else if (err.request) {
                console.error('Request was made but no response received:', err.request);
            } else {
                console.error('Something wrong in setting up the request:', err.message);
            }
        }
    };


    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">서클 만들기</h1>

            <div className="flex flex-col gap-4">
                <input
                    type="text"
                    name="name"
                    value={form.name}
                    onChange={handleInputChange}
                    placeholder="서클 이름"
                    className="border p-2 rounded"
                />

                <textarea
                    name="description"
                    value={form.description}
                    onChange={handleInputChange}
                    placeholder="서클 설명"
                    className="border p-2 rounded h-40"
                />

                <input
                    type="text"
                    name="tags"
                    value={form.tags}
                    onChange={handleInputChange}
                    placeholder="태그 (쉼표로 구분)"
                    className="border p-2 rounded"
                />

                <label className="flex items-center gap-2">
                    <input
                        type="checkbox"
                        name="isPublic"
                        checked={form.isPublic}
                        onChange={handleInputChange}
                    />
                    공개 서클
                </label>

                <CategorySelector
                    category={form.category}
                    detailCategory={form.detailCategory}
                    onCategoryChange={handleCategoryChange}
                    onDetailCategoryChange={handleDetailCategoryChange}
                />

                <button
                    onClick={handleSubmit}
                    className="bg-green-500 text-white px-6 py-2 rounded hover:bg-green-600"
                >
                    서클 생성
                </button>
            </div>
        </div>
    );
}