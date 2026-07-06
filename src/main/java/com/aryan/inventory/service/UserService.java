package com.aryan.inventory.service;


import java.util.List;

import com.aryan.inventory.dto.UpdateRoleRequest;
import com.aryan.inventory.dto.UserResponse;


public interface UserService {
	
	List<UserResponse> getAllUsers();
	
	UserResponse getUserById(Long id);
	
	UserResponse updateUserRole(Long id, UpdateRoleRequest request);
	
	void deleteUser(Long id);
	
	UserResponse whoAmI();


}