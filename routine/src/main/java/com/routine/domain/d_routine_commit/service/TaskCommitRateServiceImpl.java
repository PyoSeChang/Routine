package com.routine.domain.d_routine_commit.service;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.model.RoutineTask;
import com.routine.domain.c_routine.repository.RoutineRepository;
import com.routine.domain.d_routine_commit.model.CommitLog;
import com.routine.domain.d_routine_commit.model.TaskCommitRate;
import com.routine.domain.d_routine_commit.model.enums.CommitStatus;
import com.routine.domain.d_routine_commit.repository.CommitLogRepository;
import com.routine.domain.d_routine_commit.repository.TaskCommitRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskCommitRateServiceImpl implements TaskCommitRateService {
    private final TaskCommitRateRepository taskCommitRateRepository;
    private final RoutineRepository routineRepository;
    private final CommitLogRepository commitLogRepository;

    @Override
    public void saveWeeklyTaskCommitRates(LocalDate targetDate) {
        // 1. targetDate 기준 연도, 주차 계산
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int year = targetDate.getYear();
        int week = targetDate.get(weekFields.weekOfWeekBasedYear());

        // 2. 해당 주의 월요일 ~ 일요일 날짜 구하기
        LocalDate monday = targetDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        // 3. 모든 루틴 조회 (활성화 필터는 나중에 추가 예정)
        List<Routine> routines = routineRepository.findAll();
        List<TaskCommitRate> ratesToSave = new ArrayList<>();

        for (Routine routine : routines) {
            // 4. 루틴별 주간 CommitLog 가져오기
            List<CommitLog> commitLogs = commitLogRepository.findByRoutineIdAndCommitDateBetween(
                    routine.getId(), monday, sunday
            );

            if (commitLogs.isEmpty()) {
                continue; // ✅ 이 루틴은 이번 주 기록이 없으면 건너뛴다
            }

            // 5. (taskId, memberId) 기준으로 그룹핑
            Map<String, List<CommitLog>> groupedLogs = commitLogs.stream()
                    .collect(Collectors.groupingBy(log -> log.getTask().getId() + "-" + log.getMember().getId()));

            // 6. 그룹별 이행률 계산 및 객체 생성
            for (Map.Entry<String, List<CommitLog>> entry : groupedLogs.entrySet()) {
                List<CommitLog> logs = entry.getValue();
                RoutineTask task = logs.get(0).getTask();
                Member member = logs.get(0).getMember();

                int successCount = 0;
                int skipCount = 0;

                for (CommitLog log : logs) {
                    switch (log.getStatus()) {
                        case SUCCESS -> successCount++;
                        case SKIP -> skipCount++;
                        default -> {} // FAIL은 세지 않음
                    }
                }

                int denominator = 7 - skipCount;
                double rate = (denominator == 0) ? 0.0 : (double) successCount / denominator;

                TaskCommitRate taskCommitRate = TaskCommitRate.builder()
                        .routine(routine)
                        .member(member)
                        .taskId(task.getId())
                        .year(year)
                        .week(week)
                        .taskCommitRate(rate)
                        .build();

                ratesToSave.add(taskCommitRate);
            }
        }

        // 7. 모든 TaskCommitRate 저장
        if (!ratesToSave.isEmpty()) {
            taskCommitRateRepository.saveAll(ratesToSave);
        }
    }

}
