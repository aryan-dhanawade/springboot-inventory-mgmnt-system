package com.aryan.inventory.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aryan.inventory.dto.RegisterRequest;
import com.aryan.inventory.dto.UserResponse;
import com.aryan.inventory.entity.Role;
import com.aryan.inventory.entity.User;
import com.aryan.inventory.exception.UserAlreadyExistsException;
import com.aryan.inventory.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

}