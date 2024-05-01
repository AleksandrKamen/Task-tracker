package org.galaxy.tasktrackerapi.controller;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
import org.galaxy.tasktrackerapi.model.entity.Task;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks/{taskId:\\d+}")
public class TaskController {
    private final TaskService taskService;

   @GetMapping
   public Task getTask(@PathVariable("taskId") Long taskId) {
        return taskService.findById(taskId).orElseThrow(() -> new NoSuchElementException("Задача не найдна"));
   }

    @PatchMapping
    public ResponseEntity<Task> updateTask(@PathVariable long id,
                                           @Validated @RequestBody
                                           TaskUpdateDto taskUpdateDto,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //Todo Добавить обработку исключения
        }
        taskService.updateTask(taskUpdateDto);
        return ResponseEntity.noContent().build();
    }

    // Удаление задачи
    @DeleteMapping
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
