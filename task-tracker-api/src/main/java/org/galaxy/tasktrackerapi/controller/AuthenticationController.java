package org.galaxy.tasktrackerapi.controller;

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
public class AuthenticationController {
    JwtService jwtService;
    AuthService authenticationService;

    @PostMapping("registration")
    public ResponseEntity<User> registration(@RequestBody @Validated RegistrationUserDto registrationUserDto,
                                             BindingResult bindingResult) throws BindException {
        BindingUtils.handleBindingResult(bindingResult);
        var registeredUser = authenticationService.signup(registrationUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("login")
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
