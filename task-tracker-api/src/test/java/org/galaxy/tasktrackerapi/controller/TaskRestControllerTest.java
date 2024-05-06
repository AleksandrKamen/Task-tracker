package org.galaxy.tasktrackerapi.controller;

import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskRestControllerTest {
    @Mock
    TaskService taskService;

    @InjectMocks
    TaskRestController taskRestController;


    @Test
    void getTask_ReturnsTask() {
        var user = new User();
        var taskId = 1L;
        //when
        when(taskService.findByIdAndUser(user, taskId)).thenReturn(new TaskReadDto(1L, "Title test", "Description test",
                LocalDateTime.of(2024,5,5,10,10)));
        var actualResult = taskRestController.getTask(user, taskId);
        //then
        assertThat(actualResult).isEqualTo(new TaskReadDto(1L, "Title test", "Description test",
                LocalDateTime.of(2024,5,5,10,10)));
    }
    @Test
    void updateTask_TaskWasUpdated_ReturnsNoContent() throws BindException {
        var user = new User();
        var taskId = 1L;
        var taskUpdateDto = new TaskUpdateDto("Title update", "Description update", false);
        var bindingResult = new MapBindingResult(Map.of(), "taskUpdateDto");
        var actualResult = taskRestController.updateTask(user, taskId, taskUpdateDto, bindingResult);
        assertNotNull(actualResult);
        assertEquals(actualResult.getStatusCode(), HttpStatus.NO_CONTENT);
        verify(taskService).updateTask(user,1L, new TaskUpdateDto("Title update", "Description update", false));
    }

    @Test
    void updateTask_InvalidRequest_ThrowBindingException() {
        var user = new User();
        var taskId = 1L;
        var taskUpdateDto = new TaskUpdateDto("", "description test", false);
        var bindingResult = new MapBindingResult(Map.of(), "taskCreateDto");
        bindingResult.addError(new FieldError("taskUpdateDto", "title", "error"));
        var actualResult = assertThrows(BindException.class, () -> taskRestController.updateTask(user, taskId, taskUpdateDto, bindingResult));
        assertEquals(actualResult.getAllErrors(), List.of(new FieldError("taskUpdateDto", "title", "error")));
        verifyNoMoreInteractions(taskService);
    }
    @Test
    void deleteTask_TaskWasDeleted_ReturnsNoContent() {
        var user = new User();
        var taskId = 1L;
        var actualResult = taskRestController.deleteTask(user, taskId);
        assertNotNull(actualResult);
        assertEquals(actualResult.getStatusCode(), HttpStatus.NO_CONTENT);
    }


}