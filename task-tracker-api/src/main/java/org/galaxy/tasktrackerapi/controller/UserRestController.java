package org.galaxy.tasktrackerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.service.UserServise;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Контроллер для работы с пользователями")
public class UserRestController {
    private final UserServise userServise;

    @GetMapping
    @Operation(
            summary = "Получение данных о пользователе",
            security = @SecurityRequirement(name = "JWT"),
    responses = {@ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            type = "object",
                            properties = {
                                    @StringToClassMapItem(key = "id", value = Long.class),
                                    @StringToClassMapItem(key = "username", value = String.class)
                            }
                    )
            )
    )})
    public ResponseEntity<?> findUser(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userServise.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
}
