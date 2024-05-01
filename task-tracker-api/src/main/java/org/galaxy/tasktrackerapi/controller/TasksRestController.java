package org.galaxy.tasktrackerapi.controller;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TasksRestController {
    private final TaskService taskService;

    //получение всех задач
    @GetMapping
    public List<TaskReadDto> getAllTasks(@RequestParam(value = "filter", required = false) Boolean iscompleted) {
        return taskService.findAllTasks(iscompleted);
    }

    // Добавление задачи
    @PostMapping
    public ResponseEntity<?> createTask(@Validated @RequestBody TaskCreateDto taskCreateDto,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriBuilder) {
        if (bindingResult.hasErrors()) {
            //Todo Добавить обработку исключения
        }
        var newTask = taskService.createTask(taskCreateDto);
        return ResponseEntity
                .created(uriBuilder.replacePath("/api/v1/tasks/{taskId}")
                        .build(Map.of("taskId", newTask.getId()))
                ).body(newTask);
    }
}
