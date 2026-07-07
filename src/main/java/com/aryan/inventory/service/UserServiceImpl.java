package com.aryan.inventory.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aryan.inventory.dto.UpdateRoleRequest;
import com.aryan.inventory.dto.UserResponse;
import com.aryan.inventory.entity.Role;
import com.aryan.inventory.entity.User;
import com.aryan.inventory.exception.CannotDeleteCurrentUserException;
import com.aryan.inventory.exception.CannotModifyOwnRoleException;
import com.aryan.inventory.exception.InternalServerException;
import com.aryan.inventory.exception.UserNotFoundException;
import com.aryan.inventory.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(this::mapUserToResponse).toList();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException((id)));

		return mapUserToResponse(user);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public UserResponse updateUserRole(Long id, UpdateRoleRequest request) {
		User existing = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

		String currentUsername = getCurrentUsername();

		if (existing.getRole() == Role.ADMIN && request.getRole() != Role.ADMIN
				&& userRepository.countByRole(Role.ADMIN) == 1) {
			throw new CannotModifyOwnRoleException();
		}
		
		if(existing.getRole() == request.getRole()) {
		    return mapUserToResponse(existing);
		}

		existing.setRole(request.getRole());

		User saved = userRepository.save(existing);

		return mapUserToResponse(saved);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void deleteUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException((id)));

		String currentUsername = getCurrentUsername();

		if (user.getUsername().equals(currentUsername)) {
			throw new CannotDeleteCurrentUserException();

		}

		long adminCount = userRepository.countByRole(Role.ADMIN);

		if (user.getRole() == Role.ADMIN && adminCount == 1) {
			throw new CannotDeleteCurrentUserException();
		}

		userRepository.delete(user);

	}

	@PreAuthorize("isAuthenticated()")
	public UserResponse whoAmI() {
		User user = userRepository.findByUsername(getCurrentUsername()).orElseThrow(InternalServerException::new);

		return mapUserToResponse(user);
	}

	private UserResponse mapUserToResponse(User user) {
		UserResponse response = new UserResponse();

		response.setId(user.getId());
		response.setUsername(user.getUsername());
		response.setRole(user.getRole());

		return response;
	}

	private String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return authentication.getName();
	}

}