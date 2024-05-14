package org.galaxy.tasktrackerapi.auth.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.galaxy.tasktrackerapi.auth.dto.LoginUserDto;
import org.galaxy.tasktrackerapi.auth.dto.MessageDto;
import org.galaxy.tasktrackerapi.auth.dto.RegistrationUserDto;
import org.galaxy.tasktrackerapi.exception.PasswordIncorrectException;
import org.galaxy.tasktrackerapi.exception.UserNotFoundException;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.repository.UserRepository;
import org.galaxy.tasktrackerapi.model.service.UserServise;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService {
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    UserServise userServise;
    MessageSource messageSource;
    MessageProducerService messageProducerService;

    public User signup(RegistrationUserDto registrationUserDto) {
        log.info("Signing up user {}", registrationUserDto.getUsername());
        messageProducerService.sendMessage(MessageDto.builder()
                .email(registrationUserDto.getUsername())
                .title(messageSource.getMessage("message.welcome.title",
                        new Object[]{0}, LocaleContextHolder.getLocale()))
                .text(messageSource.getMessage("message.welcome",
                        new Object[]{0}, LocaleContextHolder.getLocale()))
                .build());
        return userServise.createUser(registrationUserDto);
    }

    public User authenticate(LoginUserDto input) {
        var userOptional = userRepository.findByUsername(input.getUsername());
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException(messageSource.getMessage("user.errors.user_not_found",
                    new Object[]{input.getUsername()}, LocaleContextHolder.getLocale()));
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    ));
        } catch (BadCredentialsException e) {
            throw new PasswordIncorrectException(messageSource.getMessage("user.login.error.incorrect_password",
                    new Object[]{input.getUsername()}, LocaleContextHolder.getLocale()));
        }

        log.info("User authenticated: {}", userOptional.get().getUsername());
        return userOptional.get();
    }
}
