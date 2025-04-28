import React from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { Category } from '../../types/board';
import { detailCategories, DetailCategoryType } from '../../types/detailCategories';

// 상위 카테고리 목록
const categories: { key: Category; label: string }[] = [
    { key: Category.LANGUAGE,   label: '외국어' },
    { key: Category.EMPLOYMENT, label: '취업' },
    { key: Category.STUDY,      label: '학습' },
    { key: Category.LIFE,       label: '라이프' },
];

export default function BoardNav() {
    const [searchParams] = useSearchParams();
    const selectedCategory = (searchParams.get('category') as Category) || '';
    const selectedDetail   = searchParams.get('detailCategory') || '';

    return (
        <nav className="w-48 border p-4">
            {/* 대분류 */}
            <h3 className="font-semibold mb-2">카테고리</h3>
            <ul className="flex flex-col space-y-1">
                {categories.map(cat => (
                    <li key={cat.key}>
                        <Link
                            to={{
                                pathname: '/boards',
                                search: `?category=${cat.key}`
                            }}
                            className={`block px-2 py-1 rounded hover:bg-gray-100 ${
                                selectedCategory === cat.key ? 'font-bold bg-gray-200' : ''
                            }`}
                        >
                            {cat.label}
                        </Link>
                    </li>
                ))}
            </ul>

            {/* 소분류 */}
            {selectedCategory && (
                <>
                    <h4 className="font-semibold mt-4 mb-2 text-sm">세부 카테고리</h4>
                    <ul className="flex flex-col space-y-1">
                        {detailCategories
                            .filter((dc: DetailCategoryType) => dc.parentCategory === selectedCategory)
                            .map((dc: DetailCategoryType) => (
                                <li key={dc.code}>
                                    <Link
                                        to={{
                                            pathname: '/boards',
                                            search: `?category=${selectedCategory}&detailCategory=${dc.code}`
                                        }}
                                        className={`block px-2 py-1 rounded hover:bg-gray-100 ${
                                            selectedDetail === dc.code ? 'font-bold bg-gray-200' : ''
                                        }`}
                                    >
                                        {dc.displayName}
                                    </Link>
                                </li>
                            ))}
                    </ul>
                </>
            )}
        </nav>
    );
}
