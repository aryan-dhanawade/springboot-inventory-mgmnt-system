package com.aryan.inventory.dto;

import com.aryan.inventory.entity.Role;

import jakarta.validation.constraints.NotNull;

public class UpdateRoleRequest {
	
	@NotNull
	private Role role;

	public UpdateRoleRequest(@NotNull Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	
	
	
	
}
