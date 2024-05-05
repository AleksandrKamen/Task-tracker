package org.galaxy.tasktrackerapi.model.service;

import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
import org.galaxy.tasktrackerapi.model.entity.User;

import java.util.List;

public interface TaskService {
    TaskReadDto createTask(User user, TaskCreateDto taskCreateDto);
    List<TaskReadDto> findAllTasks(User user, Boolean iscompleted);
    void updateTask(User user, Long taskId, TaskUpdateDto taskUpdateDto);
    void deleteTask(User user, Long taskId);
    TaskReadDto findByIdAndUser(User user, Long taskId);
}
