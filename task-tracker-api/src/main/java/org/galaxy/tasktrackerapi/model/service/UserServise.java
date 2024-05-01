package org.galaxy.tasktrackerapi.model.service;

import org.galaxy.tasktrackerapi.model.dto.UserCreateDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServise extends UserDetailsService {
      User createUser(UserCreateDto userCreateDto);
}
