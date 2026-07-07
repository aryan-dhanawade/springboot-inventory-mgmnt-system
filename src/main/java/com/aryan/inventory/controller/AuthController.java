package com.aryan.inventory.controller;

import org.springframework.web.bind.annotation.*;

import com.aryan.inventory.dto.LoginRequest;
import com.aryan.inventory.dto.LoginResponse;
import com.aryan.inventory.dto.RegisterRequest;
import com.aryan.inventory.dto.UserResponse;

import com.aryan.inventory.service.AuthenticationService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthenticationService authService;

    public AuthController( AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid RegisterRequest request) {

        return authService.register(request);

    }
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
    	return authService.login(request);
    }
}