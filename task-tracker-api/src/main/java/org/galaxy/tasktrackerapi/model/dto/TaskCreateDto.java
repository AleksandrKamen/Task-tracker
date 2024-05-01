package org.galaxy.tasktrackerapi.model.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.galaxy.tasktrackerapi.validation.FieldEquals;

@Value
@FieldEquals(field="rawPassword", equalsTo="confirmPassword")
public class TaskCreateDto {
    @NotNull
    @Size(min=3, max=50)
    String title;
    @NotNull
    @Size(max=1000)
    String description;
}

