package com.aryan.inventory.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aryan.inventory.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import com.aryan.inventory.dto.UpdateRoleRequest;
import com.aryan.inventory.dto.UserResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public List<UserResponse> getAllUsers(){
		return  userService.getAllUsers();
	}
	
	@GetMapping("/{id}")
	public UserResponse getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
		
	}
	
	@PatchMapping("/{id}/role")
	UserResponse updateUserRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request) {
		
		return userService.updateUserRole(id, request);
		
	}
	
	@DeleteMapping("/{id}")
	void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		
	}
	
	@GetMapping("/whoami")
	public UserResponse whoAmI() {
		return userService.whoAmI();
	}
}
