package org.galaxy.tasktrackerapi.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginUserDto {
    @Email(message = "{users.create.errors.username_is_not_email}")
    String username;
    @NotNull(message = "{users.create.errors.password_is_null}")
    @Size(min = 3, max = 10, message = "{users.create.errors.password_size_is_invalid}")
    String password;
}
