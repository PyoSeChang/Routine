import React, { useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { Category } from '../../types/board';
import { detailCategories, DetailCategoryType } from '../../types/detailCategories';
import { ChevronDown } from 'lucide-react';

const COLOR_MAP: Record<number, string> = {
    0: '#fff9c4', // 연노랑
    1: '#ffe0b2', // 주황
    2: '#f8bbd0', // 핑크
    3: '#c8e6c9', // 연두
    4: '#b3e5fc', // 하늘
    5: '#d1c4e9', // 보라
};

const categories: { key: Category; label: string }[] = [
    { key: Category.LANGUAGE, label: '외국어' },
    { key: Category.EMPLOYMENT, label: '취업' },
    { key: Category.STUDY, label: '학습' },
    { key: Category.LIFE, label: '라이프' },
];

export default function PostItBoardNav() {
    const [searchParams] = useSearchParams();
    const selectedCategory = (searchParams.get('category') as Category) || '';
    const selectedDetail = searchParams.get('detailCategory') || '';

    const [openMap, setOpenMap] = useState<Record<Category, boolean>>(() => {
        const initial: Record<Category, boolean> = {
            [Category.LANGUAGE]: true,
            [Category.EMPLOYMENT]: true,
            [Category.STUDY]: true,
            [Category.LIFE]: true,
            [Category.NONE]: false,
        };
        return initial;
    });

    const toggle = (cat: Category) => {
        setOpenMap(prev => ({ ...prev, [cat]: !prev[cat] }));
    };

    return (
        <nav className="w-64 flex flex-col gap-4">
            {categories.map((cat, idx) => {
                const isOpen = openMap[cat.key];
                const details = detailCategories.filter(dc => dc.parentCategory === cat.key);
                const color = COLOR_MAP[idx % Object.keys(COLOR_MAP).length] || COLOR_MAP[0];

                return (
                    <div
                        key={cat.key}
                        className=" shadow-lg px-4 py-3"
                        style={{ backgroundColor: color }}
                    >
                        <button
                            onClick={() => toggle(cat.key)}
                            className="w-full flex justify-between items-center text-left text-lg font-semibold mb-2"
                        >
                            <span>{cat.label}</span>
                            <ChevronDown
                                size={18}
                                className={`transition-transform duration-200 ${isOpen ? 'rotate-180' : ''}`}
                            />
                        </button>

                        {isOpen && (
                            <ul className="text-sm flex flex-col gap-1">
                                {details.map(dc => (
                                    <li key={dc.code}>
                                        <Link
                                            to={`/boards?category=${cat.key}&detailCategory=${dc.code}`}
                                            className={`block px-2 py-1 rounded hover:bg-gray-100 ${
                                                selectedDetail === dc.code ? 'font-bold bg-gray-200' : ''
                                            }`}
                                        >
                                            {dc.displayName}
                                        </Link>
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>
                );
            })}
        </nav>
    );
}