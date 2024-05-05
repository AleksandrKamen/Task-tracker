package org.galaxy.tasktrackerapi.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.galaxy.tasktrackerapi.validation.FieldEquals;

@Value
@FieldEquals(field="rawPassword", equalsTo="confirmPassword", message = "{users.create.errors.passwords_not_equals}")
public class RegistrationUserDto {
    @NotNull( message = "{users.create.errors.username_is_null}")
    @NotBlank(message = "{users.create.errors.username_is_blank}")
    @Email(message = "{users.create.errors.username_is_not_email}")
    String username;
    @NotNull(message = "{users.create.errors.password_is_null}")
    @Size(min = 3, max = 10, message = "{users.create.errors.password_size_is_invalid}")
    String rawPassword;
    @NotNull(message = "{users.create.errors.password_is_null}")
    @Size(min = 3, max = 10, message = "{users.create.errors.password_size_is_invalid}")
    String confirmPassword;
}

