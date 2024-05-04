package org.galaxy.tasktrackerapi.model.mapper;

import org.galaxy.tasktrackerapi.auth.dto.RegistrationUserDto;
import org.galaxy.tasktrackerapi.model.dto.UserReadDto;
import org.galaxy.tasktrackerapi.model.entity.Role;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
     @Autowired
    protected PasswordEncoder passwordEncoder;

    public User userCreateDtoToUser(RegistrationUserDto registrationUserDto){
       return User.builder()
               .username(registrationUserDto.getUsername())
               .password(passwordEncoder.encode(registrationUserDto.getRawPassword()))
               .role(Role.USER)
               .build();
    }

    public abstract UserReadDto userToUserReadDto(User user);
}
