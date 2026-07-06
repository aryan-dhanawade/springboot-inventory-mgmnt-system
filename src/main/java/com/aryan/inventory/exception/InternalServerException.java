package com.aryan.inventory.exception;

public class InternalServerException extends RuntimeException{
	
	public InternalServerException() {
		super("Unknown Server Error");
	}

}
