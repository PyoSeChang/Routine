package com.routine.domain.c_routine.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;


@Getter
@AllArgsConstructor
public class CommitLogDTO {
    private String routineName;
    private List<TaskDTO> tasks;

}