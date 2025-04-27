// types/routineRates.ts

export interface DailyRateDTO {
    date: string; // "2025-04-27"
    commitRate: number; // 0.0 ~ 1.0
}

export interface WeeklyRateDTO {
    week: string; // "4월 2주차"
    commitRate: number; // 0.0 ~ 1.0
}

export interface TaskWeeklyRateDTO {
    taskId: number;
    taskContent: string;
    rates: WeeklyRateDTO[];
}

export interface RoutineCommitRatesResponse {
    thisWeekDailyRates: DailyRateDTO[];
    lastWeekDailyRates: DailyRateDTO[];
    routineWeeklyRates: WeeklyRateDTO[];
    taskWeeklyRates: TaskWeeklyRateDTO[];
}
