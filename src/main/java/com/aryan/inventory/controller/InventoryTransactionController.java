package com.aryan.inventory.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aryan.inventory.dto.InventoryTransactionRequest;
import com.aryan.inventory.dto.InventoryTransactionResponse;
import com.aryan.inventory.service.InventoryTransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class InventoryTransactionController {

	private final InventoryTransactionService inventoryTransactionService;

	public InventoryTransactionController(InventoryTransactionService inventoryTransactionService) {
		this.inventoryTransactionService = inventoryTransactionService;

	}

	@PostMapping
	public InventoryTransactionResponse createTransaction(@RequestBody @Valid InventoryTransactionRequest request) {
		return inventoryTransactionService.createTransaction(request);
	}
	
	@GetMapping("/{id}")
	public InventoryTransactionResponse getTransactionById(@PathVariable Long id) {
		return inventoryTransactionService.getTransactionById(id);
	}

	@GetMapping
	public List<InventoryTransactionResponse> getAllTransaction() {
		return inventoryTransactionService.getAllTransactions();

	}

	@GetMapping("/products/{productId}")
	public List<InventoryTransactionResponse> getTransactionByProductId(@PathVariable Long productId) {
		return inventoryTransactionService.getTransactionsByProduct(productId);

	}
	
	

}
