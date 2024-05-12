package org.galaxy.tasktrackerapi.controller;

import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TasksRestControllerTest {
    @Mock
    TaskService taskService;
    @InjectMocks
    TasksRestController tasksRestController;

    @Test
    void getAllTasks_iscompleted_null_ReturnsAllTasks() {
        Boolean iscompleted = null;
        var user = new User();
        when(taskService.findAllTasks(user, iscompleted)).thenReturn(
                List.of(
                new TaskReadDto(1L, "Название задачи №1", "Описание задачи №1", false,
                        LocalDateTime.of(2024,5,5,10,10), null),
                new TaskReadDto(2L, "Название задачи №2", "Описание задачи №2", false,
                        LocalDateTime.of(2024,5,5,12,10),null))
        );
        var allTasks = tasksRestController.getAllTasks(user, iscompleted);
        assertEquals(allTasks,
                List.of(
                new TaskReadDto(1L, "Название задачи №1", "Описание задачи №1", false,
                        LocalDateTime.of(2024,5,5,10,10),null),
                new TaskReadDto(2L, "Название задачи №2", "Описание задачи №2", false,
                        LocalDateTime.of(2024,5,5,12,10),null))
        );
    }
    @Test
    void createTask_ReturnsTask() throws BindException {
        var user = new User();
        var taskCreateDto = new TaskCreateDto("Title test", "description test");
        var bindingResult = new MapBindingResult(Map.of(), "taskCreateDto");
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        when(taskService.createTask(user, taskCreateDto))
                .thenReturn(new TaskReadDto(1L, "Title test", "description test", false,
                        LocalDateTime.of(2024,5,5,10,10),null));
        var actualResult = tasksRestController.createTask(user, taskCreateDto, bindingResult, uriComponentsBuilder);
        assertNotNull(actualResult);
        assertEquals(actualResult.getStatusCode(), HttpStatus.CREATED);
        assertEquals(actualResult.getHeaders().getLocation(), URI.create("http://localhost/api/v1/tasks/1"));
        assertEquals(actualResult.getBody(), new TaskReadDto(1L, "Title test", "description test", false,
                LocalDateTime.of(2024,5,5,10,10),null));
        verifyNoMoreInteractions(taskService);

    }
    @Test
    void createTask_InvalidRequest_ThrowBindingException() {
        var user = new User();
        var taskCreateDto = new TaskCreateDto("", "description test");
        var bindingResult = new MapBindingResult(Map.of(), "taskCreateDto");
        bindingResult.addError(new FieldError("taskCreateDto", "title", "error"));
        var uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost");
        var actualResult = assertThrows(BindException.class, () -> tasksRestController.createTask(user, taskCreateDto, bindingResult, uriComponentsBuilder));
        assertEquals(actualResult.getAllErrors(), List.of(new FieldError("taskCreateDto", "title", "error")));
        verifyNoMoreInteractions(taskService);
    }



}