package org.galaxy.tasktrackerapi.model.service;

import org.galaxy.tasktrackerapi.auth.dto.RegistrationUserDto;
import org.galaxy.tasktrackerapi.model.dto.UserReadDto;
import org.galaxy.tasktrackerapi.model.entity.User;

public interface UserServise  {
      User createUser(RegistrationUserDto registrationUserDto);
      UserReadDto findByUsername(String username);
}
