package org.galaxy.tasktrackerapi.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.galaxy.tasktrackerapi.validation.FieldEquals;

@Value
@FieldEquals(field="rawPassword", equalsTo="confirmPassword")
public class UserCreateDto {
    @Email
    String username;
    @NotBlank
    @Size(min = 3, max = 10)
    String rawPassword;
    @NotBlank
    @Size(min = 3, max = 10)
    String confirmPassword;
}

