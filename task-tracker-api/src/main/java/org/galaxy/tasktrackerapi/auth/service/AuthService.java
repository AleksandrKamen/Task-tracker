package org.galaxy.tasktrackerapi.auth.service;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.auth.dto.LoginUserDto;
import org.galaxy.tasktrackerapi.auth.dto.MessageDto;
import org.galaxy.tasktrackerapi.auth.dto.RegistrationUserDto;
import org.galaxy.tasktrackerapi.exception.UserNotFoundException;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.repository.UserRepository;
import org.galaxy.tasktrackerapi.model.service.UserServise;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private  final UserServise userServise;
    private final MessageSource messageSource;
    private final MessageProducerService messageProducerService;

    public User signup(RegistrationUserDto registrationUserDto) {
       return userServise.createUser(registrationUserDto);
    }

    public User authenticate(LoginUserDto input) {
        var userOptional = userRepository.findByUsername(input.getUsername());
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException(messageSource.getMessage("user.errors.user_not_found",
                    new Object[]{input.getUsername()}, LocaleContextHolder.getLocale()));
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );
        messageProducerService.sendMessage(MessageDto.builder()
                        .email(input.getUsername())
                        .title("Верификация вашей почты")
                        .text("%s Добро пожаловать на наш сервис".formatted(input.getUsername()))
                .build());


        return userOptional.get();
    }
}
