package org.galaxy.tasktrackerapi.controller;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.model.service.UserServise;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserServise userServise;


    @GetMapping
    public ResponseEntity<?> findUser(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userServise.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
}
