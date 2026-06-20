package com.prangyajeet.mvep.auth.controller;

import com.prangyajeet.mvep.auth.dto.LoginRequest;
import com.prangyajeet.mvep.auth.dto.LoginResponse;
import com.prangyajeet.mvep.auth.dto.RegisterRequest;
import com.prangyajeet.mvep.auth.dto.RegisterResponse;
import com.prangyajeet.mvep.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}