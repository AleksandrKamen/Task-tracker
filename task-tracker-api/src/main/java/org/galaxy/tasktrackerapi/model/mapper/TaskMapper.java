package org.galaxy.tasktrackerapi.model.mapper;

import org.galaxy.tasktrackerapi.model.dto.TaskCreateDto;
import org.galaxy.tasktrackerapi.model.dto.TaskReadDto;
import org.galaxy.tasktrackerapi.model.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task taskCreateDtoToTask(TaskCreateDto taskCreateDto);
    TaskReadDto taskToTaskReadDto(Task task);
}
