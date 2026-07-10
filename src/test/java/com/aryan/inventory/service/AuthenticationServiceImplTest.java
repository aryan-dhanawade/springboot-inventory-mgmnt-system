package com.aryan.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aryan.inventory.dto.LoginRequest;
import com.aryan.inventory.dto.LoginResponse;
import com.aryan.inventory.dto.RegisterRequest;
import com.aryan.inventory.dto.UserResponse;
import com.aryan.inventory.entity.Role;
import com.aryan.inventory.entity.User;
import com.aryan.inventory.exception.UserAlreadyExistsException;
import com.aryan.inventory.repository.UserRepository;
import com.aryan.inventory.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtService jwtService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private AuthenticationServiceImpl authenticationService;

	@Test
	void shouldRegisterSuccessfully() {

		RegisterRequest request = createRegisterRequest();

		when(userRepository.existsByUsername("admin")).thenReturn(false);

		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

		User savedUser = createUser();

		savedUser.setPassword("encodedPassword");

		when(userRepository.save(any(User.class))).thenReturn(savedUser);

		UserResponse response = authenticationService.register(request);

		assertEquals(1L, response.getId());
		assertEquals("admin", response.getUsername());
		assertEquals(Role.EMPLOYEE, response.getRole());

		verify(userRepository).existsByUsername("admin");
		verify(passwordEncoder).encode("password");
		verify(userRepository).save(any(User.class));
	}

	@Test
	void shouldThrowUserAlreadyExistsException() {

		RegisterRequest request = createRegisterRequest();

		when(userRepository.existsByUsername("admin")).thenReturn(true);

		assertThrows(UserAlreadyExistsException.class, () -> authenticationService.register(request));

		verify(userRepository).existsByUsername("admin");
		verify(passwordEncoder, never()).encode(any());
		verify(userRepository, never()).save(any());
	}

	@Test
	void shouldEncodePasswordBeforeSaving() {

		RegisterRequest request = createRegisterRequest();

		when(userRepository.existsByUsername("admin")).thenReturn(false);

		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

		when(userRepository.save(any(User.class))).thenReturn(createUser());

		authenticationService.register(request);

		verify(passwordEncoder).encode("password");
	}

	@Test
	void shouldLoginSuccessfully() {

		LoginRequest request = createLoginRequest();
		User user = createUser();

		when(authenticationManager.authenticate(any())).thenReturn(authentication);

		when(authentication.getPrincipal()).thenReturn(user);

		when(jwtService.generateToken(user)).thenReturn("jwt-token");

		LoginResponse response = authenticationService.login(request);

		assertEquals("jwt-token", response.getToken());
		assertEquals("admin", response.getUsername());
		assertEquals("EMPLOYEE", response.getRole());

		verify(authenticationManager).authenticate(any());
		verify(jwtService).generateToken(user);
	}

	@Test
	void shouldThrowExceptionForInvalidCredentials() {

		LoginRequest request = createLoginRequest();

		when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid"));

		assertThrows(BadCredentialsException.class, () -> authenticationService.login(request));

		verify(jwtService, never()).generateToken(any());
	}

	@Test
	void shouldGenerateJwtTokenAfterLogin() {

		LoginRequest request = createLoginRequest();
		User user = createUser();

		when(authenticationManager.authenticate(any())).thenReturn(authentication);

		when(authentication.getPrincipal()).thenReturn(user);

		when(jwtService.generateToken(user)).thenReturn("jwt-token");

		authenticationService.login(request);

		verify(jwtService).generateToken(user);
	}

	private RegisterRequest createRegisterRequest() {

		RegisterRequest request = new RegisterRequest();

		request.setUsername("admin");
		request.setPassword("password");

		return request;
	}

	private LoginRequest createLoginRequest() {

		LoginRequest request = new LoginRequest();

		request.setUsername("admin");
		request.setPassword("password");

		return request;
	}

	private User createUser() {

		User user = new User();

		user.setId(1L);
		user.setUsername("admin");
		user.setPassword("encodedPassword");
		user.setRole(Role.EMPLOYEE);

		return user;
	}
}