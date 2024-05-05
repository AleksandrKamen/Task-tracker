package org.galaxy.tasktrackerapi.model.service;

import org.galaxy.tasktrackerapi.auth.dto.RegistrationUserDto;
import org.galaxy.tasktrackerapi.exception.UserAlreadyExistException;
import org.galaxy.tasktrackerapi.exception.UserNotFoundException;
import org.galaxy.tasktrackerapi.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
class UserServiceTest {
    static final String USERNAME_TEST = "test@gmail.com";
    static final String PASSWORD_TEST = "test";
    @Autowired
    UserServise userService;
    @Autowired
    UserRepository userRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");

    @Test
    void createUser_ReturnsCreatedUser() {
        var registrationUserDto = new RegistrationUserDto(USERNAME_TEST, PASSWORD_TEST, PASSWORD_TEST);
        var userCreate = userService.createUser(registrationUserDto);
        var actualResult = userRepository.findByUsername(USERNAME_TEST);
        assertTrue(actualResult.isPresent());
        assertTrue(BCrypt.checkpw(PASSWORD_TEST, actualResult.get().getPassword()));
    }

    @Test
    @Sql("/sql/user.sql")
    void createUser_UserAlreadyExists_ThrowsUserAlreadyExistException() {
        var registrationUserDto = new RegistrationUserDto(USERNAME_TEST, PASSWORD_TEST, PASSWORD_TEST);
        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(registrationUserDto));
    }

    @Test
    @Sql("/sql/user.sql")
    void findUserByUsername_UserExist_ReturnsUserReadDto() {
        var userReadDto = userService.findByUsername(USERNAME_TEST);
        assertEquals(userReadDto.getId(), 1L);
        assertEquals(userReadDto.getUsername(), USERNAME_TEST);
    }

    @Test
    void findUserByUsername_UserNotExist_ThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.findByUsername(USERNAME_TEST));
    }
}