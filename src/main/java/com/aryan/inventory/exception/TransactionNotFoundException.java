package com.aryan.inventory.exception;

public class TransactionNotFoundException extends RuntimeException {
	
	public TransactionNotFoundException(long id) {
		super("TRANSACTION " + id + " not found!");
	}

}
