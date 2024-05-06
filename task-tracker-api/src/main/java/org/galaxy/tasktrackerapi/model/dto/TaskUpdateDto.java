package org.galaxy.tasktrackerapi.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class TaskUpdateDto {
    @NotNull(message = "{tasks.create.errors.title_is_null}")
    @Size(min=3, max=50, message = "{tasks.create.errors.title_size_is_invalid}")
    String title;
    @NotNull(message = "{tasks.create.errors.description_is_null}")
    @Size(max=1000, message = "{tasks.create.errors.description_size_is_invalid}")
    String description;
    Boolean iscomplited;
}

