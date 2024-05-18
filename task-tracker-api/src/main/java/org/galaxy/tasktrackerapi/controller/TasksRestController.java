package org.galaxy.tasktrackerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.service.TaskService;
import org.galaxy.tasktrackerapi.validation.BindingUtils;
import org.springframework.http.MediaType;
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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Контроллер по работе с задачами")
public class TasksRestController {
    TaskService taskService;

    @GetMapping
    @Operation(
            summary = "Получение всех задач",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            [
                                                              {
                                                                "id": 1,
                                                                "title": "Новая задача",
                                                                "description": "Описание задачи",
                                                                "iscompleted": false,
                                                                "createdAt": "2024-05-18T11:35:00.864066",
                                                                "completed_at": null
                                                              },
                                                                "id": 2,
                                                                "title": "Новая задача 2",
                                                                "description": "Описание задачи 2",
                                                                "iscompleted": false,
                                                                "createdAt": "2024-05-18T11:35:00.864066",
                                                                "completed_at": null
                                                              }
                                                            ]
                                                            """
                                            )}
                            )
                    }
            )
    })
    public List<TaskReadDto> getAllTasks(@AuthenticationPrincipal User user,
                                         @RequestParam(value = "filter", required = false) Boolean iscompleted) {
        return taskService.findAllTasks(user, iscompleted);
    }

    @PostMapping
    @Operation(
            summary = "Создание новой задачи",
            security = @SecurityRequirement(name = "JWT"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    properties = {
                                            @StringToClassMapItem(key = "title", value = String.class),
                                            @StringToClassMapItem(key = "description", value = String.class),
                                    }
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
                                                                    {
                                                                      "id": 1,
                                                                      "title": "example title",
                                                                      "description": "example description",
                                                                      "iscompleted": false,
                                                                      "createdAt": "2024-05-18T10:05:11.182Z",
                                                                      "completed_at": "null"
                                                                    }
                                                                    """
                                                    )}
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            examples = {
                                                    @ExampleObject(
                                                            value = """
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
                                                                    """
                                                    )}
                                    )
                            }
                    ),

            })
    public ResponseEntity<?> createTask(@AuthenticationPrincipal User user,
                                        @Validated @RequestBody TaskCreateDto taskCreateDto,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriBuilder) throws BindException {
        BindingUtils.handleBindingResult(bindingResult);
        var newTask = taskService.createTask(user, taskCreateDto);
        return ResponseEntity
                .created(uriBuilder.replacePath("/api/v1/tasks/{taskId}")
                        .build(Map.of("taskId", newTask.getId()))
                ).body(newTask);
    }
}
