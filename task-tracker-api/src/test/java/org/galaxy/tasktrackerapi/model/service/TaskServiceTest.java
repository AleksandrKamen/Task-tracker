package org.galaxy.tasktrackerapi.model.service;

import org.galaxy.tasktrackerapi.exception.TaskNotFoundException;
import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
import org.galaxy.tasktrackerapi.model.repository.TaskRepository;
import org.galaxy.tasktrackerapi.model.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
class TaskServiceTest {
    static final String USERNAME_TEST = "test@gmail.com";
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @Sql("/sql/user.sql")
    void createTask_ReturnsTaskReadDto() {
        var user = userRepository.findByUsername(USERNAME_TEST).get();
        var taskCreateDto = new TaskCreateDto("TitleTest", "Description Test");
        var task = taskService.createTask(user, taskCreateDto);
        var actualResult = taskRepository.findByIdAndUser(1L, user);
        assertTrue(actualResult.isPresent());
        assertEquals(actualResult.get().getTitle(), "TitleTest");
        assertEquals(actualResult.get().getDescription(), "Description Test");
    }
    @Nested
    @Sql("/sql/user.sql")
    @Sql("/sql/tasks.sql")
    class findAllTasks{
        @Test
        void findAllTasks_iscompleted_null_returnsAllCompletedTasks() {
            var user = userRepository.findByUsername(USERNAME_TEST).get();
            var allTasks = taskService.findAllTasks(user, null);
            assertThat(allTasks.size()).isEqualTo(5);
        }
        @Test
        void findAllTasks_iscompleted_true_returnsAllCompletedTasks() {
            var user = userRepository.findByUsername(USERNAME_TEST).get();
            var allCompletedTasks = taskService.findAllTasks(user, true);
            assertThat(allCompletedTasks.size()).isEqualTo(3);
        }
        @Test
        void findAllTasks_iscompleted_false_returnsAllCompletedTasks() {
            var user = userRepository.findByUsername(USERNAME_TEST).get();
            var allNotCompletedTasks = taskService.findAllTasks(user, false);
            assertThat(allNotCompletedTasks.size()).isEqualTo(2);
        }
    }
    @Test
    @Sql("/sql/user.sql")
    @Sql("/sql/tasks.sql")
    void deleteTask_TaskWasDeleted() {
        var user = userRepository.findByUsername(USERNAME_TEST).get();
        assertTrue(taskRepository.existsById(1L));
        taskService.deleteTask(user, 1L);
        assertFalse(taskRepository.existsById(1L));
    }
    @Test
    @Sql("/sql/user.sql")
    void deleteTask_TaskNotExist_ThrowsTaskNotFoundException() {
        var user = userRepository.findByUsername(USERNAME_TEST).get();
        assertThrows(TaskNotFoundException.class, ()-> taskService.deleteTask(user, 1L));
    }
    @Test
    @Sql("/sql/user.sql")
    @Sql("/sql/tasks.sql")
    void updateTask_TaskWasUpdated() {
        var user = userRepository.findByUsername(USERNAME_TEST).get();
        var taskUpdateDto = new TaskUpdateDto("TitleUpdate", "Description Update", false);
        taskService.updateTask(user, 1L, taskUpdateDto);
        var actualResult = taskRepository.findByIdAndUser(1L, user).get();
        assertEquals(actualResult.getTitle(), "TitleUpdate");
        assertEquals(actualResult.getDescription(), "Description Update");
    }
    @Test
    @Sql("/sql/user.sql")
    void updateTask_TaskNotExist_ThrowsTaskNotFoundException() {
        var user = userRepository.findByUsername(USERNAME_TEST).get();
        var taskUpdateDto = new TaskUpdateDto("TitleUpdate", "Description Update", false);
        assertThrows(TaskNotFoundException.class, ()-> taskService.updateTask(user, 1L, taskUpdateDto));
    }



}