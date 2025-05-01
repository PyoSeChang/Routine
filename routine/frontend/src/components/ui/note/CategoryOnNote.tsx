import React, { useState, useRef, useEffect } from 'react';
import { Category } from '../../../types/board';
import { detailCategories } from '../../../types/detailCategories';
import Line from './Line';

interface CategoryOnNoteProps {
    category: Category;
    detailCategory: string;
    onCategoryChange: (category: Category) => void;
    onDetailCategoryChange: (detailCategory: string) => void;
}

const categories: { label: string; value: Category }[] = [
    { label: '카테고리를 선택해주세요.', value: Category.NONE },
    { label: '외국어', value: Category.LANGUAGE },
    { label: '취업', value: Category.EMPLOYMENT },
    { label: '학습', value: Category.STUDY },
    { label: '라이프', value: Category.LIFE },
];

const CategoryOnNote: React.FC<CategoryOnNoteProps> = ({
                                                           category,
                                                           detailCategory,
                                                           onCategoryChange,
                                                           onDetailCategoryChange,
                                                       }) => {
    const [isCatOpen, setCatOpen] = useState(false);
    const [isDetailOpen, setDetailOpen] = useState(false);

    const catRef = useRef<HTMLDivElement>(null);
    const detailRef = useRef<HTMLDivElement>(null);

    const filteredDetailCategories = detailCategories.filter(
        (detail) => detail.parentCategory === category
    );

    // 외부 클릭 시 드롭다운 닫기
    useEffect(() => {
        const handleClickOutside = (e: MouseEvent) => {
            if (catRef.current && !catRef.current.contains(e.target as Node)) {
                setCatOpen(false);
            }
            if (detailRef.current && !detailRef.current.contains(e.target as Node)) {
                setDetailOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    return (
        <div className="flex flex-col bg-note w-full">
            {/* 카테고리 드롭다운 */}
            <Line className="relative" ref={catRef}>
                <span className="shrink-0 font-ui text-base whitespace-pre">카테고리: </span>
                <button
                    onClick={() => setCatOpen((prev) => !prev)}
                    className="w-full flex justify-between items-center text-left font-ui text-base focus:outline-none"
                >
                    <span>{categories.find((c) => c.value === category)?.label}</span>
                    <span className="text-sm text-gray-500">▾</span>
                </button>

                {isCatOpen && (
                    <ul className="absolute top-full left-0 w-full bg-note z-10 shadow border border-blue-200">
                        {categories.map((cat) => (
                            <Line
                                key={cat.value}
                                className={`font-ui cursor-pointer hover:bg-blue-100 ${
                                    cat.value === category ? 'bg-blue-50 font-semibold' : ''
                                } ${cat.value === Category.NONE ? 'text-gray-400' : ''}`}
                                onClick={() => {
                                    onCategoryChange(cat.value);
                                    onDetailCategoryChange('');
                                    setCatOpen(false);
                                }}
                            >
                                {cat.label}
                            </Line>
                        ))}
                    </ul>
                )}
            </Line>

            {/* 디테일 카테고리 드롭다운 */}
            <Line className="relative" ref={detailRef}>
                <span className="shrink-0 font-ui text-base whitespace-pre">세부 카테고리: </span>
                <button
                    onClick={() => setDetailOpen((prev) => !prev)}
                    className="w-full flex justify-between items-center text-left font-ui text-base focus:outline-none"
                >
          <span>
            {detailCategories.find((d) => d.code === detailCategory)?.displayName ||
                '세부 카테고리를 선택하세요'}
          </span>
                    <span className="text-sm text-gray-500">▾</span>
                </button>

                {isDetailOpen && (
                    <ul className="absolute top-full left-0 w-full bg-note z-10 shadow border border-blue-200">
                        {detailCategory === '' && (
                            <Line className="text-gray-400 font-ui">세부 카테고리를 선택해주세요.</Line>
                        )}
                        {filteredDetailCategories.map((detail) => (
                            <Line
                                key={detail.code}
                                className={`font-ui cursor-pointer hover:bg-blue-100 ${
                                    detail.code === detailCategory ? 'bg-blue-50 font-semibold' : ''
                                }`}
                                onClick={() => {
                                    onDetailCategoryChange(detail.code);
                                    setDetailOpen(false);
                                }}
                            >
                                {detail.displayName}
                            </Line>
                        ))}
                    </ul>
                )}
            </Line>
        </div>
    );
};

export default CategoryOnNote;
