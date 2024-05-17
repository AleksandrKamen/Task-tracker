package org.galaxy.tasktrackerapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("dev")
@Transactional
@Sql("/sql/user.sql")
@Sql("/sql/tasks.sql")
public class TaskRestControllerTestIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "test@gmail.com")
    void getTask_ReturnsTask() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/tasks/1");
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                        "id": 1,
                        "title": 'Первая задача',
                        "description": 'Описание первой задачи',
                        "iscompleted": true,
                        "createdAt": '2024-05-05T12:00:00',
                        "completed_at": '2024-05-05T14:00:00'
                        }
                        """)
        );
    }

    @Test
    @WithUserDetails(value = "test@gmail.com")
    void getTask_TaskNotExist_ThrowNotFoundException() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/tasks/10");
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    void getTask_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/tasks/1");
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    @WithUserDetails(value = "test@gmail.com")
    void updateTask_RequestIsValid_ReturnsNonContent() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.patch("/api/v1/tasks/1").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "title": "Новое название",
                "description": "Новаое описание",
                "iscompleted": false
                }
                """);
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    @WithUserDetails(value = "test@gmail.com")
    void updateTask_RequestIsInvalid_ReturnsBadRequest() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.patch("/api/v1/tasks/1").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "title": "",
                "description": "Новое описание",
                "iscompleted": false
                }
                """);
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
                content().json("""
                        {
                            "type": "about:blank",
                            "title": "Bad Request",
                            "status": 400,
                            "detail": "Ошибка запроса",
                            "instance": "/api/v1/tasks/1",
                            "errors": [
                                "Название задачи должно быть от 3 до 50 символов"
                            ]
                        }
                        """)
        );
    }

    @Test
    @WithUserDetails(value = "test@gmail.com")
    void updateTask_TaskNotExist_ThrowNotFoundException() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.patch("/api/v1/tasks/10").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "title": "Новое название",
                "description": "Новаое описание",
                "iscompleted": false
                }
                """);
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    @WithUserDetails(value = "test@gmail.com")
    void deleteTask_TaskExist_ReturnsNonContent() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/api/v1/tasks/1");
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNoContent()
        );
    }

    @Test
    @WithUserDetails(value = "test@gmail.com")
    void deleteTask_TaskNotExist_ReturnsNotFound() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/api/v1/tasks/10");
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    @WithUserDetails(value = "test2@gmail.com")
    void deleteTask_UserNotOwner_ReturnsNotFound() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/api/v1/tasks/1");
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
        );
    }


}
