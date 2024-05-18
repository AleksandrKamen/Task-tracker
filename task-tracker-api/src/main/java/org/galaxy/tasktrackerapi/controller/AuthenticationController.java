package org.galaxy.tasktrackerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.galaxy.tasktrackerapi.auth.dto.LoginResponse;
import org.galaxy.tasktrackerapi.auth.dto.LoginUserDto;
import org.galaxy.tasktrackerapi.auth.dto.RegistrationUserDto;
import org.galaxy.tasktrackerapi.auth.service.AuthService;
import org.galaxy.tasktrackerapi.auth.service.JwtService;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.validation.BindingUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Контроллер авторизации")
public class AuthenticationController {
    JwtService jwtService;
    AuthService authenticationService;

    @PostMapping("registration")
    @Operation(summary = "Регистрация пользователей", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "id": 4,
                                                      "username": "example@mail",
                                                      "password": "$2a$10$T.qRobOqeX4ijOrPKKEVBOL0lJlEsUUFWQ3DfPCm0XX8b8xOnDDSK",
                                                      "createdAt": "2024-05-18T10:36:31.759+00:00",
                                                      "updatedAt": "2024-05-18T10:36:31.759+00:00",
                                                      "role": "USER",
                                                      "tasks": null,
                                                      "enabled": true,
                                                      "authorities": [
                                                        {
                                                          "authority": "USER"
                                                        }
                                                      ],
                                                      "accountNonLocked": true,
                                                      "credentialsNonExpired": true,
                                                      "accountNonExpired": true
                                                    }
                                                    """
                                    )}
                    )
            ),
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
                                                      "instance": "/api/v1/auth/registration",
                                                      "errors": [
                                                        "Имя пользователя должно быть почтой",
                                                        "Пароль должен содержать от 3 до 10 символов",
                                                        "Пароль и проверочный пароль не совпадают"
                                                      ]
                                                    }
                                                    """
                                    )}
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "type": "about:blank",
                                                      "title": "Conflict",
                                                      "status": 409,
                                                      "detail": "Пользователь уже существует",
                                                      "instance": "/api/v1/auth/registration",
                                                      "errors": "Пользователь с именем example@mail уже существует"
                                                    }
                                                    """
                                    )}
                    )
            )
    })
    public ResponseEntity<User> registration(@RequestBody @Validated RegistrationUserDto registrationUserDto,
                                             BindingResult bindingResult) throws BindException {
        BindingUtils.handleBindingResult(bindingResult);
        var registeredUser = authenticationService.signup(registrationUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("login")
    @Operation(summary = "Авторизация пользователей", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "token": "exapb4ciOiJIUzI1NiJ9.eyJzdWIiOiIyMjJAc3NzIiwiaWF0IjoxNzE2MDI4NzYyLCJleHAiOjE3MTYwMzIzNjJ9.dzkOasi2t47STWTbykBVDmeI5zMW9QopUAbnDHAmZwQ",
                                                      "expiresIn": 1716032362754
                                                    }
                                                    """
                                    )}
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "type": "about:blank",
                                                      "title": "Unauthorized",
                                                      "status": 401,
                                                      "detail": "Ошибка авторизации",
                                                      "instance": "/api/v1/auth/login",
                                                      "errors": "Неверный пароль"
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
                                                      "instance": "/api/v1/auth/login",
                                                      "errors": "Пользователь с именем string@sdsad не найден"
                                                    }
                                                    """
                                    )}
                    )
            )
    })
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Validated LoginUserDto loginUserDto,
                                                      BindingResult bindingResult) throws BindException {
        BindingUtils.handleBindingResult(bindingResult);
        var user = authenticationService.authenticate(loginUserDto);
        var jwtToken = jwtService.generateToken(user);
        var loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(System.currentTimeMillis() + jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }
}
