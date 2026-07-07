package com.aryan.inventory.dto;

import com.aryan.inventory.entity.TransactionType;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class InventoryTransactionRequest {
	
	@NotNull(message = "Product ID is required")
	private Long productId;
	
	
	@NotNull(message = "Transaction type is required")
	private  TransactionType type;
	
	
	@NotNull(message = "Quantity is required")
	@Positive(message = "Quantity must be greater than 0")
	private Integer quantity;
	
	@Size(max = 255, message = "Remarks cannot exceed 255 characters")
	private String remarks;

	public InventoryTransactionRequest() {}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
	
	
}
