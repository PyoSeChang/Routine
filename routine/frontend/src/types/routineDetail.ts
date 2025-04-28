// types/routineDetail.ts

export interface RoutineDTO {
    routineId: number;
    circleId?: number;
    title: string;
    category: string;
    detailCategory: string;
    tags?: string;
    description?: string;
    repeatDays: string[]; // 예: ["MONDAY", "WEDNESDAY"]
    createdAt: string; // ISO 문자열
    circleRoutine: boolean;
    tasks: string[]; // 태스크 내용 리스트
}

export type DayOfWeek =
    | 'MONDAY'
    | 'TUESDAY'
    | 'WEDNESDAY'
    | 'THURSDAY'
    | 'FRIDAY'
    | 'SATURDAY'
    | 'SUNDAY';
