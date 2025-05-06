import { Category, BoardType } from '../types/board';
import { detailCategories } from '../types/detailCategories';

// Category displayName → enum
export const displayNameToCategoryMap: Record<string, Category> = {
    '외국어': Category.LANGUAGE,
    '취업': Category.EMPLOYMENT,
    '학습': Category.STUDY,
    '라이프': Category.LIFE,
    '없음': Category.NONE,
};

// BoardType displayName → enum
export const displayNameToBoardTypeMap: Record<string, BoardType> = {
    '공지': BoardType.NOTICE,
    '홍보': BoardType.PROMOTION,
    '후기': BoardType.REVIEW,
    '질문답변': BoardType.QNA,
    '정보': BoardType.INFORMATION,
};

// DetailCategory displayName → code string
export const displayNameToDetailCategoryCodeMap: Record<string, string> = {};
detailCategories.forEach(dc => {
    displayNameToDetailCategoryCodeMap[dc.displayName] = dc.code;
});
