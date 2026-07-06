package com.aryan.inventory.exception;

public class CannotDeleteCurrentUserException extends RuntimeException {
	public CannotDeleteCurrentUserException() {
		super("Cannot Delete Current User");
	}
}
