package org.galaxy.tasktrackerapi.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
