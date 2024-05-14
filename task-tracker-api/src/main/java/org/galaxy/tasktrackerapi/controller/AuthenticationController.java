package org.galaxy.tasktrackerapi.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.galaxy.tasktrackerapi.auth.dto.LoginResponse;
import org.galaxy.tasktrackerapi.auth.dto.LoginUserDto;
import org.galaxy.tasktrackerapi.auth.dto.MessageDto;
import org.galaxy.tasktrackerapi.auth.dto.RegistrationUserDto;
import org.galaxy.tasktrackerapi.auth.service.MessageProducerService;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.auth.service.AuthService;
import org.galaxy.tasktrackerapi.auth.service.JwtService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationController {
     JwtService jwtService;
     AuthService authenticationService;
     MessageProducerService messageProducerService;
     MessageSource messageSource;


    @PostMapping("registration")
    public ResponseEntity<User> registration(@RequestBody @Validated RegistrationUserDto registrationUserDto,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        var registeredUser = authenticationService.signup(registrationUserDto);
        messageProducerService.sendMessage(MessageDto.builder()
                .email(registrationUserDto.getUsername())
                .title(messageSource.getMessage("message.welcome.title",
                        new Object[]{0}, LocaleContextHolder.getLocale()))
                .text(messageSource.getMessage("message.welcome",
                        new Object[]{0}, LocaleContextHolder.getLocale()))
                .build());
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Validated LoginUserDto loginUserDto,
                                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        var user = authenticationService.authenticate(loginUserDto);
        var jwtToken = jwtService.generateToken(user);
        var loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(System.currentTimeMillis() + jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }
}
