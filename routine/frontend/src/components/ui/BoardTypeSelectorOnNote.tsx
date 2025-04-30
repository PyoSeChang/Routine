// components/ui/BoardTypeSelectorOnNote.tsx
import React from 'react';
import { BoardType } from '../../types/board';
import DropdownOnNote from './DropdownOnNote';

interface Props {
    value: BoardType;
    onChange: (value: BoardType) => void;
}

const boardTypeLabels: Record<BoardType, string> = {
    [BoardType.NOTICE]: '공지',
    [BoardType.PROMOTION]: '홍보',
    [BoardType.REVIEW]: '후기',
    [BoardType.QNA]: 'Q&A',
    [BoardType.INFORMATION]: '정보공유',
};

const options = Object.entries(BoardType).map(([key, value]) => ({
    label: boardTypeLabels[value as BoardType],
    value: value,
}));

export default function BoardTypeSelectorOnNote({ value, onChange }: Props) {
    return (
        <DropdownOnNote
            label="게시판 종류"
            value={value}
            options={options}
            onChange={(val) => onChange(val as BoardType)}
            placeholder="게시판 종류 선택"
        />
    );
}
