export interface TaskDTO {
    taskId: number;
    content: string;
    status: 'SUCCESS' | 'FAIL' | 'SKIP' | 'NONE' ;
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
    date: string;
    weekday: Weekday;
    repeatDays: Weekday[];
    type: 'PAST' | 'TODAY' | 'UPCOMING';
    groupRoutine: boolean; // ← ✅ 이름 맞춰야 함
    routineSkipped: boolean;      // ← ✅ 이름 맞춰야 함
    tasks: TaskDTO[];
}
