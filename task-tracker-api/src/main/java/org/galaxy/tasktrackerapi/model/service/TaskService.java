package org.galaxy.tasktrackerapi.model.service;

import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(TaskCreateDto taskCreateDto);
    List<Task> findAllTasks();
    List<Task> findAllTasksByIsCompletedOrIsNotCompleted(Boolean iscompleted);
    void updateTask(Long taskId, String title, String description);
    void deleteTask(Long taskId);

}
