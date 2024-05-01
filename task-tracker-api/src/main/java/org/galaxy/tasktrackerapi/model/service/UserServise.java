package org.galaxy.tasktrackerapi.model.service;

import org.galaxy.tasktrackerapi.model.dto.UserCreateDto;
import org.galaxy.tasktrackerapi.model.dto.UserReadDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserServise extends UserDetailsService {
      User createUser(UserCreateDto userCreateDto);
      UserReadDto findByUsername(String username);
}
