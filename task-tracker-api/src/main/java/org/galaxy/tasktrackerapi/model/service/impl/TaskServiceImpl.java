package org.galaxy.tasktrackerapi.model.service.impl;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.entity.Task;
import org.galaxy.tasktrackerapi.model.mapper.TaskMapper;
import org.galaxy.tasktrackerapi.model.repository.TaskRepository;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    @Override
    public Task createTask(TaskCreateDto taskCreateDto) {
        return taskRepository.save(taskMapper.taskCreateDtoToTask(taskCreateDto));
    }

    @Override
    public List<Task> findAllTasks() {
        return List.of();
    }

    @Override
    public List<Task> findAllTasksByIsCompletedOrIsNotCompleted(Boolean iscompleted) {
        return List.of();
    }

    @Transactional
    @Override
    public void deleteTask(Long taskId) {
      taskRepository.deleteById(taskId);
    }

    @Transactional
    @Override
    public void updateTask(Long taskId, String title, String description) {
        taskRepository.findById(taskId).ifPresentOrElse(task -> {
           task.setTitle(title);
           task.setDescription(description);
        },()-> {
            throw new NoSuchElementException();
        });
    }
}
