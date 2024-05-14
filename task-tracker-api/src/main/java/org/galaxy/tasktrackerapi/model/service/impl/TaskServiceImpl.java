package org.galaxy.tasktrackerapi.model.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galaxy.tasktrackerapi.exception.TaskNotFoundException;
import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.mapper.TaskMapper;
import org.galaxy.tasktrackerapi.model.repository.TaskRepository;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final MessageSource messageSource;

    @Transactional
    @Override
    public TaskReadDto createTask(User user, TaskCreateDto taskCreateDto) {
        log.info("user {} create task with title: {}", user.getUsername(), taskCreateDto.getTitle());
        var task = taskMapper.taskCreateDtoToTask(taskCreateDto);
        task.setUser(user);
        return taskMapper.taskToTaskReadDto(taskRepository.save(task));
    }

    @Override
    public TaskReadDto findByIdAndUser(User user, Long taskId) {
        return taskMapper.taskToTaskReadDto(taskRepository.findByIdAndUser(taskId, user).orElseThrow(
                () -> new TaskNotFoundException(messageSource.getMessage("tasks.errors.not_found",
                        new Object[0], LocaleContextHolder.getLocale())))
        );
    }

    @Override
    public List<TaskReadDto> findAllTasks(User user, Boolean iscompleted) {
        return iscompleted == null ? taskRepository.findByUser(user).stream()
                .map(taskMapper::taskToTaskReadDto)
                .toList() :
                taskRepository.findByIscompletedAndUser(iscompleted, user).stream()
                        .map(taskMapper::taskToTaskReadDto)
                        .toList();
    }

    @Transactional
    @Override
    public void deleteTask(User user, Long taskId) {
        log.info("user {} delete task with id: {}", user.getUsername(), taskId);
        taskRepository.findByIdAndUser(taskId, user).ifPresentOrElse(taskRepository::delete,
                () -> {
                    throw new TaskNotFoundException(messageSource.getMessage("tasks.errors.not_found",
                            new Object[0], LocaleContextHolder.getLocale()));
                });

    }

    @Transactional
    @Override
    public void updateTask(User user, Long taskId, TaskUpdateDto taskUpdateDto) {
        log.info("user {} update task with id: {}", user.getUsername(), taskId);
        taskRepository.findByIdAndUser(taskId, user).ifPresentOrElse(task -> {
            task.setTitle(taskUpdateDto.getTitle());
            task.setDescription(taskUpdateDto.getDescription());
            if (taskUpdateDto.getIscompleted() != null && task.getIscompleted() != taskUpdateDto.getIscompleted()) {
                task.setIscompleted(taskUpdateDto.getIscompleted());
                task.setCompleted_at(task.getIscompleted() ? LocalDateTime.now() : null);
            }
        }, () -> {
            throw new TaskNotFoundException(messageSource.getMessage("tasks.errors.not_found",
                    new Object[0], LocaleContextHolder.getLocale()));
        });
    }
}
