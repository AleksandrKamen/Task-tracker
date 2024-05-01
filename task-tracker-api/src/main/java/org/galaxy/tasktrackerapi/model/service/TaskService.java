package org.galaxy.tasktrackerapi.model.service;

import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
import org.galaxy.tasktrackerapi.model.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(TaskCreateDto taskCreateDto);
    List<TaskReadDto> findAllTasks(Boolean iscompleted);
    void updateTask(TaskUpdateDto taskUpdateDto);
    void deleteTask(Long taskId);
    Optional<Task> findById(Long taskId);
}
