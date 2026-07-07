package com.aryan.inventory.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aryan.inventory.dto.InventoryTransactionRequest;
import com.aryan.inventory.dto.InventoryTransactionResponse;
import com.aryan.inventory.entity.InventoryTransaction;
import com.aryan.inventory.entity.Product;
import com.aryan.inventory.entity.User;
import com.aryan.inventory.exception.InsufficientStockException;
import com.aryan.inventory.exception.InternalServerException;
import com.aryan.inventory.exception.ProductNotFoundException;
import com.aryan.inventory.exception.TransactionNotFoundException;
import com.aryan.inventory.repository.InventoryTransactionRepository;
import com.aryan.inventory.repository.ProductRepository;
import com.aryan.inventory.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class InventoryTransactionServiceImpl implements InventoryTransactionService {
	
	private final InventoryTransactionRepository inventoryTransactionRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	
	public InventoryTransactionServiceImpl(ProductRepository productRepository, UserRepository userRepository, InventoryTransactionRepository inventoryTransactionRepository) {
		this.inventoryTransactionRepository = inventoryTransactionRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		
	}

	@Override
	@Transactional
	@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
	public InventoryTransactionResponse createTransaction(InventoryTransactionRequest request) {
		System.out.println(request.getProductId());
		Long productId = request.getProductId();
		Product product = productRepository.findById(productId).orElseThrow(
					() -> new ProductNotFoundException(productId)
				);
		
		String username = getCurrentUsername();
		User currentUser = userRepository.findByUsername(username).orElseThrow(InternalServerException::new);
	
		Product updatedProduct = processTransaction(product, request);
		
		
		InventoryTransaction transaction = new InventoryTransaction();
		transaction.setProduct(updatedProduct);
		transaction.setQuantity(request.getQuantity());
		transaction.setRemarks(request.getRemarks());
		transaction.setPerformedBy(currentUser);
		transaction.setType(request.getType());
		transaction.setTimestamp(LocalDateTime.now());
	
		productRepository.save(updatedProduct);
		InventoryTransaction saved = inventoryTransactionRepository.save(transaction);
	
		return mapTransactionToResponse(saved);
		
	}

	@PreAuthorize("isAuthenticated()")
	@Override
	public List<InventoryTransactionResponse> getAllTransactions() {
			return inventoryTransactionRepository.findAll()
					.stream()
					.map(this::mapTransactionToResponse)
					.toList();
					
	}
	
	@PreAuthorize("isAuthenticated()")
	@Override
	public List<InventoryTransactionResponse> getTransactionsByProduct(Long productId) {
		return inventoryTransactionRepository.findByProductId(productId)
				.stream()
				.map(this::mapTransactionToResponse)
				.toList();
	}
	
	@PreAuthorize("isAuthenticated()")
	@Override
	public InventoryTransactionResponse getTransactionById(Long id) {
			InventoryTransaction transaction = inventoryTransactionRepository.findById(id).orElseThrow(
					() -> new TransactionNotFoundException(id));
					
			return mapTransactionToResponse(transaction);

	}
	
	private String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return authentication.getName();
	}
	
	private InventoryTransactionResponse mapTransactionToResponse(
	        InventoryTransaction transaction) {

	    InventoryTransactionResponse response =
	            new InventoryTransactionResponse();

	    response.setId(transaction.getId());
	    response.setProduct(
	            transaction.getProduct().getName()
	    );
	    response.setQuantity(transaction.getQuantity());
	    response.setType(transaction.getType());
	    response.setPerformedBy(
	            transaction.getPerformedBy().getUsername()
	    );
	    response.setRemarks(transaction.getRemarks());
	    response.setTimestamp(transaction.getTimestamp());

	    return response;
	}
	
	private Product processTransaction(Product product, InventoryTransactionRequest request) {
		
		switch(request.getType()) {

	     case PURCHASE:
	     case RETURN:
	        product.setQuantity(
	            product.getQuantity() + request.getQuantity()
	        );
	        break;

	     case SALE:
	     case DAMAGE:

	        if(product.getQuantity() < request.getQuantity()) {
	            throw new InsufficientStockException(request.getType());
	        }

	        product.setQuantity(
	            product.getQuantity() - request.getQuantity()
	        );

	        break;

	     case ADJUSTMENT:
	    	product.setQuantity(request.getQuantity());
	}
		return product;
	}
	
}
