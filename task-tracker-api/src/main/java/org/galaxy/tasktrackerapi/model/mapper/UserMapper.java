package org.galaxy.tasktrackerapi.model.mapper;

import org.galaxy.tasktrackerapi.model.dto.UserCreateDto;
import org.galaxy.tasktrackerapi.model.dto.UserReadDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
     User userCreateDtoToUser(UserCreateDto userCreateDto);
     UserReadDto userToUserReadDto(User user);
}
