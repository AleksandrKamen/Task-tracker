package org.galaxy.tasktrackerapi.model.service.impl;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
import org.galaxy.tasktrackerapi.model.entity.Task;
import org.galaxy.tasktrackerapi.model.mapper.TaskMapper;
import org.galaxy.tasktrackerapi.model.repository.TaskRepository;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public Optional<Task> findById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public List<TaskReadDto> findAllTasks(Boolean iscompleted) {
        if (iscompleted == null) {
             var tasks = (List<Task>)  taskRepository.findAll();
             return tasks.stream().map(taskMapper::taskToTaskReadDto).toList();
        } else {
            var tasks = taskRepository.findAllByIscomplited(iscompleted);
            return tasks.stream().map(taskMapper::taskToTaskReadDto).toList();
        }
    }

    @Transactional
    @Override
    public void deleteTask(Long taskId) {
      taskRepository.deleteById(taskId);
    }



    @Transactional
    @Override
    public void updateTask(TaskUpdateDto taskUpdateDto) {
        taskRepository.findById(taskUpdateDto.getId()).ifPresentOrElse(task -> {
           task.setTitle(taskUpdateDto.getTitle());
           task.setDescription(taskUpdateDto.getDescription());
        },()-> {
            throw new NoSuchElementException();
        });
    }
}
