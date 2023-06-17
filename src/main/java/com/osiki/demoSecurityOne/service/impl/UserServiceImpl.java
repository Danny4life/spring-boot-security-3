package com.osiki.demoSecurityOne.service.impl;

import com.osiki.demoSecurityOne.config.JwtService;
import com.osiki.demoSecurityOne.dto.*;
import com.osiki.demoSecurityOne.entity.User;
import com.osiki.demoSecurityOne.enums.Role;
import com.osiki.demoSecurityOne.repository.UserRepository;
import com.osiki.demoSecurityOne.service.UserService;
import com.osiki.demoSecurityOne.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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
               .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
               .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
               .registrationInfo(RegistrationInfo.builder()

                       .firstname(user.getFirstname())
                       .lastname(user.getLastname())
                       .email(user.getEmail())
                       .token(jwtToken)
                       .build())

               //.token(jwtToken)
               .build();
    }

    @Override
    public LoginResponse login(LoginRequestDto loginRequestDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return LoginResponse.builder()
                .responseCode(AccountUtils.LOGIN_SUCCESS_CODE)
                .responseMessage(AccountUtils.LOGIN_SUCCESS_MESSAGE)
                .loginInfo(LoginInfo.builder()
                        .email(user.getEmail())
                        .token(jwtToken)
                        .build())
                //.token(jwtToken)
                .build();
    }
}
