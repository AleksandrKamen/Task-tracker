package org.galaxy.tasktrackerapi.model.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.galaxy.tasktrackerapi.auth.dto.RegistrationUserDto;
import org.galaxy.tasktrackerapi.exception.UserAlreadyExistException;
import org.galaxy.tasktrackerapi.exception.UserNotFoundException;
import org.galaxy.tasktrackerapi.model.dto.UserReadDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.mapper.UserMapper;
import org.galaxy.tasktrackerapi.model.repository.UserRepository;
import org.galaxy.tasktrackerapi.model.service.UserServise;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserServise {
    UserRepository userRepository;
    UserMapper userMapper;
    MessageSource messageSource;

    @Transactional
    @Override
    public User createUser(RegistrationUserDto registrationUserDto) {
        if (userRepository.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException(messageSource.getMessage("user.create.errors.user_already_exist",
                    new Object[]{registrationUserDto.getUsername()}, LocaleContextHolder.getLocale()));
        }
        return userRepository.save(userMapper.userCreateDtoToUser(registrationUserDto));
    }

    @Override
    public UserReadDto findByUsername(String username) {
        return userMapper.userToUserReadDto(
                userRepository.findByUsername(username).orElseThrow(() ->
                        new UserNotFoundException(messageSource.getMessage("user.errors.user_not_found",
                        new Object[]{username}, LocaleContextHolder.getLocale())))
        );
    }
}
