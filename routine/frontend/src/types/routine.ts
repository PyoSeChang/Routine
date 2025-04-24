export interface TaskDTO {
    taskId: number;
    content: string;
    status: 'SUCCESS' | 'FAIL' | 'SKIP' | null;
    checked: boolean;
}

export type Weekday =
    | 'MONDAY' | 'TUESDAY' | 'WEDNESDAY'
    | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';

export interface RoutineViewDTO {
    routineId: number;
    title: string;
    category: string;
    detailCategory: string;
    description: string;
    date: string; // ISO date string
    weekday: Weekday;
    repeatDays: Weekday[];
    type: 'PAST' | 'TODAY' | 'UPCOMING';
    isGroupRoutine: boolean;
    isSubmitted: boolean;        // ✅ 추가된 필드
    tasks: TaskDTO[];
}
