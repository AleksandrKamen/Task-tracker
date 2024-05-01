package org.galaxy.tasktrackerapi.controller;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.dto.UserCreateDto;
import org.galaxy.tasktrackerapi.model.service.UserServise;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserServise userServise;


    @GetMapping
    public ResponseEntity<?> findUser(Principal principal) {
        if (principal != null) {
            var user = userServise.findByUsername(principal.getName());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<?> createUser(@Validated @RequestBody UserCreateDto userCreateDtol,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            //Todo Валидация ошибок
        }
        // Todo регистрация пользователя
        return null;
    }


}
