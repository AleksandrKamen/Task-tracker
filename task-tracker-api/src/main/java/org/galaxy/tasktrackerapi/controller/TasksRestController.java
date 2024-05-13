package org.galaxy.tasktrackerapi.controller;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
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

    @GetMapping
    public List<TaskReadDto> getAllTasks(@AuthenticationPrincipal User user,
                                         @RequestParam(value = "filter", required = false) Boolean iscompleted) {
        return taskService.findAllTasks(user, iscompleted);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@AuthenticationPrincipal User user,
                                        @Validated @RequestBody TaskCreateDto taskCreateDto,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        var newTask = taskService.createTask(user, taskCreateDto);
        return ResponseEntity
                .created(uriBuilder.replacePath("/api/v1/tasks/{taskId}")
                        .build(Map.of("taskId", newTask.getId()))
                ).body(newTask);
    }
}
