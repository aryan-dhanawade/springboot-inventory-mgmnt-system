package com.aryan.inventory.exception;

import com.aryan.inventory.entity.TransactionType;

public class InsufficientStockException extends RuntimeException {
	
	public InsufficientStockException(TransactionType type) {
		super("Insufficent Stock to perform TRANSACTION: " + type.name());
	}

}
