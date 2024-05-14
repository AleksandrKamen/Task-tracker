package org.galaxy.tasktrackerapi.controller;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.galaxy.tasktrackerapi.validation.BindingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks/{taskId:\\d+}")
public class TaskRestController {
    private final TaskService taskService;

    @GetMapping
    public TaskReadDto getTask(@AuthenticationPrincipal User user,
                               @PathVariable("taskId") Long taskId) {
        return taskService.findByIdAndUser(user, taskId);
    }

    @PatchMapping
    public ResponseEntity<?> updateTask(@AuthenticationPrincipal User user,
                                        @PathVariable("taskId") Long taskId,
                                        @Validated @RequestBody
                                        TaskUpdateDto taskUpdateDto,
                                        BindingResult bindingResult) throws BindException {
        BindingUtils.handleBindingResult(bindingResult);
        taskService.updateTask(user, taskId, taskUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTask(@AuthenticationPrincipal User user,
                                           @PathVariable("taskId") Long taskId) {
        taskService.deleteTask(user, taskId);
        return ResponseEntity.noContent().build();
    }
}
