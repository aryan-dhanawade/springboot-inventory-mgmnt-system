package com.aryan.inventory.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aryan.inventory.dto.LoginRequest;
import com.aryan.inventory.dto.LoginResponse;
import com.aryan.inventory.dto.RegisterRequest;
import com.aryan.inventory.dto.UserResponse;
import com.aryan.inventory.entity.Role;
import com.aryan.inventory.entity.User;
import com.aryan.inventory.exception.UserAlreadyExistsException;
import com.aryan.inventory.repository.UserRepository;
import com.aryan.inventory.security.JwtService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService,
			UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		;
	}

	@Override
	public LoginResponse login(LoginRequest request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = (User) authentication.getPrincipal();

		String token = jwtService.generateToken(user );
		
		
		return new LoginResponse(token, user.getUsername(), user.getRole().toString());
	}

	public UserResponse register(RegisterRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new UserAlreadyExistsException(request.getUsername());
		}

		User user = new User();

		user.setUsername(request.getUsername());

		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setRole(Role.EMPLOYEE);

		User newUser = userRepository.save(user);

		return userToResponseMapping(newUser);
	}

	private UserResponse userToResponseMapping(User user) {

		UserResponse response = new UserResponse();

		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setRole(user.getRole());

		return response;

	}
}