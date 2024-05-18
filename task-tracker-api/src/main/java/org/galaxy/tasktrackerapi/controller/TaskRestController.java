package org.galaxy.tasktrackerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.dto.TaskUpdateDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks/{taskId:\\d+}")
@Tag(name = "Контроллер по работе с выбранной задачей")
public class TaskRestController {
    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Получение задачи по id", security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                            "id": 1,
                                                            "title": "Название задачи",
                                                            "description": "Описание задачи",
                                                            "iscompleted": false,
                                                            "createdAt": "2024-05-18T11:35:00.864066",
                                                            "completed_at": null
                                                            }
                                                            """
                                            )}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                              "type": "about:blank",
                                                              "title": "Not Found",
                                                              "status": 404,
                                                              "detail": "По запросу ничего не найдено",
                                                              "instance": "/api/v1/tasks/1",
                                                              "errors": "Задача не найдена"
                                                            }
                                                            """
                                            )}
                            )
                    )})
    public TaskReadDto getTask(@AuthenticationPrincipal User user,
                               @PathVariable("taskId") Long taskId) {
        return taskService.findByIdAndUser(user, taskId);
    }

    @PatchMapping
    @Operation(summary = "Изменение задачи", security = @SecurityRequirement(name = "JWT"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    properties = {
                                            @StringToClassMapItem(key = "title", value = String.class),
                                            @StringToClassMapItem(key = "description", value = String.class),
                                            @StringToClassMapItem(key = "iscompleted", value = Boolean.class),
                                    }
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                              "type": "about:blank",
                                                              "title": "Bad Request",
                                                              "status": 400,
                                                              "detail": "Ошибка запроса",
                                                              "instance": "/api/v1/tasks/15",
                                                              "errors": [
                                                                "Название задачи должно быть от 3 до 50 символов"
                                                              ]
                                                            }
                                                            """
                                            )}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                              "type": "about:blank",
                                                              "title": "Not Found",
                                                              "status": 404,
                                                              "detail": "По запросу ничего не найдено",
                                                              "instance": "/api/v1/tasks/1",
                                                              "errors": "Задача не найдена"
                                                            }
                                                            """
                                            )}
                            )
                    )
            })
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
    @Operation(
            summary = "Удаление задачи по id",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                              "type": "about:blank",
                                                              "title": "Not Found",
                                                              "status": 404,
                                                              "detail": "По запросу ничего не найдено",
                                                              "instance": "/api/v1/tasks/1",
                                                              "errors": "Задача не найдена"
                                                            }
                                                            """
                                            )}
                            )
                    )

            })
    public ResponseEntity<Void> deleteTask(@AuthenticationPrincipal User user,
                                           @PathVariable("taskId") Long taskId) {
        taskService.deleteTask(user, taskId);
        return ResponseEntity.noContent().build();
    }
}
