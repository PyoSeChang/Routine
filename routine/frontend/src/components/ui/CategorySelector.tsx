// src/components/common/CategorySelector.tsx
import React from 'react';
import { Category } from '../../types/board';
import { detailCategories } from '../../types/detailCategories';

interface CategorySelectorProps {
    category: Category;
    detailCategory: string;
    onCategoryChange: (category: Category) => void;
    onDetailCategoryChange: (detailCategory: string) => void;
}

export const CategorySelector: React.FC<CategorySelectorProps> = ({
                                                                      category,
                                                                      detailCategory,
                                                                      onCategoryChange,
                                                                      onDetailCategoryChange,
                                                                  }) => {
    const filteredDetailCategories = detailCategories.filter(
        (detail) => detail.parentCategory === category
    );

    return (
        <div className="flex flex-col gap-2">
            {/* 카테고리 선택 */}
            <select
                value={category}
                onChange={(e) => onCategoryChange(e.target.value as Category)}
                className="border p-2 rounded"
            >
                <option value={Category.LANGUAGE}>외국어</option>
                <option value={Category.EMPLOYMENT}>취업</option>
                <option value={Category.STUDY}>학습</option>
                <option value={Category.LIFE}>라이프</option>
            </select>

            {/* 디테일 카테고리 선택 */}
            <select
                value={detailCategory}
                onChange={(e) => onDetailCategoryChange(e.target.value)}
                className="border p-2 rounded"
            >
                <option value="">세부 카테고리를 선택하세요</option>
                {filteredDetailCategories.map((detail) => (
                    <option key={detail.code} value={detail.code}>
                        {detail.displayName}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default CategorySelector;