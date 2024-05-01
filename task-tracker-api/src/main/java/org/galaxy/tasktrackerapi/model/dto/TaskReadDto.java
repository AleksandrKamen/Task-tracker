package org.galaxy.tasktrackerapi.model.dto;
import lombok.Value;
import java.time.LocalDateTime;

@Value
public class TaskReadDto {
    Long id;
    String title;
    String description;
    LocalDateTime createdAt;

}
