package com.routine.domain.b_circle.dto;

import com.routine.domain.c_routine.model.Routine;
import com.routine.domain.c_routine.model.RoutineTask;
import com.routine.domain.e_board.model.Category;
import com.routine.domain.e_board.model.DetailCategory;
import com.routine.domain.c_routine.dto.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class CircleRoutineDTO {

    private Long routineId;
    private String title;
    private String description;
    private Category category;
    private DetailCategory detailCategory;
    private List<TaskDTO> tasks;
    private List<String> repeatDays; // 요일을 String으로 (ex: "MONDAY", "TUESDAY")

    public static CircleRoutineDTO from(Routine routine, List<RoutineTask> routineTasks) {
        return CircleRoutineDTO.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .description(routine.getDescription())
                .category(routine.getCategory())
                .detailCategory(routine.getDetailCategory())
                .tasks(routineTasks.stream()
                        .map(TaskDTO::fromTask) // ✅ TaskDTO로 변환
                        .collect(Collectors.toList()))
                .repeatDays(routine.getRepeatDays().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList()))
                .build();
    }
}
