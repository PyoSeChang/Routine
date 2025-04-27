package com.routine.domain.c_routine.dto;

import com.routine.domain.a_member.model.Member;
import com.routine.domain.b_circle.model.Circle;
import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.model.RoutineTask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoutineDTO {
    private Long circleId;
    private String title;
    private String category;
    private String detailCategory;
    private String tags;
    private String description;
    private List<String> tasks;
    private List<DayOfWeek> repeatDays;
    private Long routineId;
    private LocalDateTime createdAt;

    public Routine toPersonalRoutine(Member member) {
        return Routine.builder()
                .title(this.title)
                .category(this.category)
                .detailCategory(this.detailCategory)
                .tags(this.tags)
                .repeatDays(this.repeatDays)
                .description(this.description)
                .isGroupRoutine(false) // 개인 루틴이므로 false 고정
                .member(member)
                .circle(null) // 개인 루틴이므로 서클 없음
                .build();
    }

    public List<RoutineTask> toRoutineTasks(Routine routine) {
        if (this.tasks == null) return List.of();

        return
                java.util.stream.IntStream.range(0, this.tasks.size())
                        .mapToObj(i -> RoutineTask.builder()
                                .content(this.tasks.get(i))
                                .orderNumber(i + 1)
                                .routine(routine)
                                .build())
                        .toList();
    }

    public Routine toCircleRoutine(Member member, Circle circle) {
        return Routine.builder()
                .title(this.title)
                .category(this.category)
                .detailCategory(this.detailCategory)
                .tags(this.tags)
                .description(this.description)
                .isGroupRoutine(true) // ✅ 서클 루틴이므로 true 고정
                .member(member)
                .circle(circle)
                .build();
    }

    public static RoutineDTO fromEntity(Routine routine, List<RoutineTask> tasks) {
        List<String> taskContents = tasks.stream()
                .sorted(Comparator.comparingInt(RoutineTask::getOrderNumber))
                .map(RoutineTask::getContent)
                .toList();

        Long circleId = (routine.getCircle() != null) ? routine.getCircle().getId() : null;

        RoutineDTO dto = new RoutineDTO();
        dto.setTitle(routine.getTitle());
        dto.setCategory(routine.getCategory());
        dto.setDetailCategory(routine.getDetailCategory());
        dto.setTags(routine.getTags());
        dto.setDescription(routine.getDescription());
        dto.setTasks(taskContents);
        dto.setRepeatDays(routine.getRepeatDays());
        dto.setCircleId(circleId);
        dto.setRoutineId(routine.getId());
        dto.setCreatedAt(routine.getCreatedAt());

        return dto;
    }

}
