import { Category } from './board';

export interface DetailCategoryType {
    code: string;            // 예: ENGLISH
    displayName: string;     // 예: 영어
    parentCategory: Category; // 예: LANGUAGE
}

export const detailCategories: DetailCategoryType[] = [
    { code: 'ENGLISH', displayName: '영어', parentCategory: Category.LANGUAGE },
    { code: 'JAPANESE', displayName: '일본어', parentCategory: Category.LANGUAGE },
    { code: 'CHINESE', displayName: '중국어', parentCategory: Category.LANGUAGE },

    { code: 'IT', displayName: 'IT', parentCategory: Category.EMPLOYMENT },
    { code: 'FINANCE', displayName: '금융', parentCategory: Category.EMPLOYMENT },
    { code: 'MANAGEMENT_OFFICE', displayName: '경영/사무', parentCategory: Category.EMPLOYMENT },
    { code: 'MARKETING_ADVERTISEMENT', displayName: '마케팅/광고', parentCategory: Category.EMPLOYMENT },
    { code: 'DESIGN', displayName: '디자인', parentCategory: Category.EMPLOYMENT },
    { code: 'CONSTRUCTION', displayName: '건설/토목', parentCategory: Category.EMPLOYMENT },
    { code: 'MANUFACTURING', displayName: '생산/제조', parentCategory: Category.EMPLOYMENT },
    { code: 'LOGISTICS', displayName: '유통/물류', parentCategory: Category.EMPLOYMENT },
    { code: 'EDUCATION', displayName: '교육', parentCategory: Category.EMPLOYMENT },
    { code: 'MEDICAL_PHARMACY_WELFARE', displayName: '의료/제약/복지', parentCategory: Category.EMPLOYMENT },
    { code: 'LEGAL', displayName: '법률/법무', parentCategory: Category.EMPLOYMENT },
    { code: 'MEDIA_CULTURE_ART', displayName: '미디어/문화/예술', parentCategory: Category.EMPLOYMENT },
    { code: 'SERVICE_SALES', displayName: '서비스/영업', parentCategory: Category.EMPLOYMENT },
    { code: 'PUBLIC_AGENCY', displayName: '공공기관/공기업', parentCategory: Category.EMPLOYMENT },

    { code: 'MAJOR', displayName: '전공 공부', parentCategory: Category.STUDY },
    { code: 'CERTIFICATE', displayName: '자격증 준비', parentCategory: Category.STUDY },

    { code: 'HEALTH', displayName: '건강', parentCategory: Category.LIFE },
    { code: 'EXERCISE', displayName: '운동', parentCategory: Category.LIFE },
    { code: 'DAILY', displayName: '데일리', parentCategory: Category.LIFE },
];
