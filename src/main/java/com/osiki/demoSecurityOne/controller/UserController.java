package com.osiki.demoSecurityOne.controller;

import com.osiki.demoSecurityOne.dto.AuthResponse;
import com.osiki.demoSecurityOne.dto.LoginRequestDto;
import com.osiki.demoSecurityOne.dto.RegisterRequestDto;
import com.osiki.demoSecurityOne.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")

    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequestDto registerRequestDto){

        return ResponseEntity.ok(userService.register(registerRequestDto));

    }

    @PostMapping("/login")

    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDto loginRequestDto){

        return ResponseEntity.ok(userService.login(loginRequestDto));

    }

}
