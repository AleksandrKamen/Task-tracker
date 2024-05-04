package org.galaxy.tasktrackerapi.auth.service;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.auth.dto.LoginUserDto;
import org.galaxy.tasktrackerapi.auth.dto.RegistrationUserDto;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.galaxy.tasktrackerapi.model.repository.UserRepository;
import org.galaxy.tasktrackerapi.model.service.UserServise;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private  final UserServise userServise;



    public User signup(RegistrationUserDto registrationUserDto) {
       return userServise.createUser(registrationUserDto);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}
