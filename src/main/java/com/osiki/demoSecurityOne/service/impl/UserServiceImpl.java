package com.osiki.demoSecurityOne.service.impl;

import com.osiki.demoSecurityOne.config.JwtService;
import com.osiki.demoSecurityOne.dto.AuthResponse;
import com.osiki.demoSecurityOne.dto.LoginRequestDto;
import com.osiki.demoSecurityOne.dto.RegisterRequestDto;
import com.osiki.demoSecurityOne.entity.User;
import com.osiki.demoSecurityOne.enums.Role;
import com.osiki.demoSecurityOne.repository.UserRepository;
import com.osiki.demoSecurityOne.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequestDto registerRequestDto) {

        User user = User.builder()
                .firstname(registerRequestDto.getFirstname())
                .lastname(registerRequestDto.getLastname())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

       var jwtToken = jwtService.generateToken(user);

       return AuthResponse.builder()
               .token(jwtToken)
               .build();
    }

    @Override
    public AuthResponse login(LoginRequestDto loginRequestDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
