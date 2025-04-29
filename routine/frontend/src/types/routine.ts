import {Category} from "./board";


export interface TaskDTO {
    taskId: number;
    content: string;
    status: 'SUCCESS' | 'NONE' | 'SKIP' | 'NULL' ;
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
    skipCount: number;
}

export interface RoutineSummaryDTO {
    routineId: number;
    title: string;
    category: Category;
    detailCategory: string;
}

