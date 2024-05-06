package org.galaxy.tasktrackerapi.model.service.impl;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.exception.TaskNotFoundException;
import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
import org.galaxy.tasktrackerapi.model.entity.Task;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.mapper.TaskMapper;
import org.galaxy.tasktrackerapi.model.repository.TaskRepository;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final MessageSource messageSource;

    @Transactional
    @Override
    public TaskReadDto createTask(User user, TaskCreateDto taskCreateDto) {
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
        if (iscompleted == null) {
            var tasks = (List<Task>) taskRepository.findByUser(user);
            return tasks.stream().map(taskMapper::taskToTaskReadDto).toList();
        } else {
            var tasks = taskRepository.findByIscomplitedAndUser(iscompleted, user);
            return tasks.stream().map(taskMapper::taskToTaskReadDto).toList();
        }
    }

    @Transactional
    @Override
    public void deleteTask(User user, Long taskId) {
       if (!taskRepository.findByIdAndUser(taskId, user).isPresent()){
           throw new TaskNotFoundException(messageSource.getMessage("tasks.errors.not_found",
                   new Object[0], LocaleContextHolder.getLocale()));
       } else {
           taskRepository.deleteById(taskId);
       }
    }


    @Transactional
    @Override
    public void updateTask(User user, Long taskId, TaskUpdateDto taskUpdateDto) {
        taskRepository.findByIdAndUser(taskId, user).ifPresentOrElse(task -> {
            task.setTitle(taskUpdateDto.getTitle());
            task.setDescription(taskUpdateDto.getDescription());
            task.setIscomplited(taskUpdateDto.getIscomplited());
        }, () -> {
            throw new TaskNotFoundException(messageSource.getMessage("tasks.errors.not_found",
                    new Object[0], LocaleContextHolder.getLocale()));
        });
    }
}
