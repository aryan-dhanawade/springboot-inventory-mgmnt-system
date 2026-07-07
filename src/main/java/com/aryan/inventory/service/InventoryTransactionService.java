package com.aryan.inventory.service;

import java.util.List;

import com.aryan.inventory.dto.InventoryTransactionRequest;
import com.aryan.inventory.dto.InventoryTransactionResponse;

public interface InventoryTransactionService {

	InventoryTransactionResponse createTransaction(InventoryTransactionRequest request);

	List<InventoryTransactionResponse> getAllTransactions();

	List<InventoryTransactionResponse> getTransactionsByProduct(Long productId);

	InventoryTransactionResponse getTransactionById(Long id);
}
