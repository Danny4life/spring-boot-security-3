package com.osiki.demoSecurityOne.service;

import com.osiki.demoSecurityOne.dto.AuthResponse;
import com.osiki.demoSecurityOne.dto.LoginRequestDto;
import com.osiki.demoSecurityOne.dto.RegisterRequestDto;

public interface UserService {
    AuthResponse register(RegisterRequestDto registerRequestDto);

    AuthResponse login(LoginRequestDto loginRequestDto);
}
