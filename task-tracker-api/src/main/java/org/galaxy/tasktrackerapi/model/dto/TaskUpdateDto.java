package org.galaxy.tasktrackerapi.model.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class TaskUpdateDto {
    Long id;
    @NotNull
    @Size(min=3, max=50)
    String title;
    @NotNull
    @Size(max=1000)
    String description;
}

