package com.aryan.inventory.exception;

public class CannotModifyOwnRoleException extends RuntimeException {
	public CannotModifyOwnRoleException() {
		super("Cannot Modify Own Role!");
	}
}
