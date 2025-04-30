import { Category } from './board';
import { Weekday } from './routine';

export interface CircleDTO {
    id?: number;
    leaderId: number;
    name: string;
    description: string;
    tags: string;
    isPublic: boolean;
    category: Category;
    detailCategory: string;

}

export interface TaskDTO {
    taskId: number;
    content: string;
    status: 'SUCCESS' | 'FAIL' | 'SKIP' | 'NONE' | 'NULL';
    checked: boolean;
}

export interface CircleRoutineDTO {
    routineId: number;
    title: string;
    description: string;
    repeatDays: Weekday[];
    tasks: TaskDTO[];
}

export interface MemberCommitInfoDTO {
    memberId: number;
    nickname: string;
    tasks: TaskDTO[];
    commitMessage?: string;
    commitRate: number;
}

export interface AuthorizationDTO {
    leader: boolean;
    member: boolean;
    circleMemberId: number;
    circleMemberName: string;
}

export interface CircleMemberSimpleDTO {
    circleMemberId: number;
    circleMemberName: string;
}

export interface CircleMemberDTO {
    memberId: number;
    nickname: string;
}
