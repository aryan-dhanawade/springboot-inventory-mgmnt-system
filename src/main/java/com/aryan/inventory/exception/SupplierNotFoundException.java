package com.aryan.inventory.exception;

public class SupplierNotFoundException extends RuntimeException {

    public SupplierNotFoundException(Long id) {
        super("Supplier with ID " + id + " not found.");
    }
}