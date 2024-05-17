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
public class TasksRestControllerTestIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "test@gmail.com")
    void getAllTasks_Returns5Tasks() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/tasks");
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                        [
                            {
                                "id": 1,
                                "title": "Первая задача",
                                "description": "Описание первой задачи",
                                "iscompleted": true,
                                "createdAt": "2024-05-05T12:00:00",
                                "completed_at": "2024-05-05T14:00:00"
                            } ,
                             {
                                "id": 2,
                                "title": "Вторая задача",
                                "description": "Описание второй задачи",
                                "iscompleted": false,
                                "createdAt": '2024-05-05T12:00:00',
                                "completed_at": null
                            },
                              {
                                "id": 3,
                                "title": "Третья задача",
                                "description": "Описание третьей задачи",
                                "iscompleted": true,
                                "createdAt": "2024-05-05T12:00:00",
                                "completed_at": "2024-05-05T18:00:00"
                            } ,
                             {
                                "id": 4,
                                "title": "Четвертая задача",
                                "description": "Описание четвертой задачи",
                                "iscompleted": false,
                                "createdAt": "2024-05-05T12:00:00",
                                "completed_at": null
                            },
                              {
                                "id": 5,
                                "title": "Пятая задача",
                                "description": "Описание пятой задачи",
                                "iscompleted": true,
                                "createdAt": "2024-05-05T12:00:00",
                                "completed_at": "2024-05-05T16:00:00"
                            }
                        ]
                        """)
        );
    }

    @Test
    @WithUserDetails(value = "test2@gmail.com")
    void getAllTasks_Returns0Tasks() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/tasks");
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("[]")
        );
    }

    @Test
    @WithUserDetails(value = "test2@gmail.com")
    void createTask_RequestInvalid_ReturnsBadRequest() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "title": "",
                "description": "Новая задача",
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
                            "instance": "/api/v1/tasks",
                            "errors": [
                                "Название задачи должно быть от 3 до 50 символов"
                            ]
                        }
                        """)
        );
    }

}
