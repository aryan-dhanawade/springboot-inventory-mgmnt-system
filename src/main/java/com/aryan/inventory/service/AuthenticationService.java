package com.aryan.inventory.service;

import com.aryan.inventory.dto.LoginRequest;
import com.aryan.inventory.dto.LoginResponse;
import com.aryan.inventory.dto.RegisterRequest;
import com.aryan.inventory.dto.UserResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);
    
    UserResponse register(RegisterRequest request);

}