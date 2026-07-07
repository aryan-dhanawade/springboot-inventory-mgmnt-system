package com.aryan.inventory.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.aryan.inventory.dto.DashboardResponse;
import com.aryan.inventory.repository.CategoryRepository;
import com.aryan.inventory.repository.InventoryTransactionRepository;
import com.aryan.inventory.repository.ProductRepository;
import com.aryan.inventory.repository.SupplierRepository;
import com.aryan.inventory.repository.UserRepository;



@Service
public class DashboardServiceImpl implements DashboardService {
	
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final SupplierRepository supplierRepository;
	private final UserRepository userRepository;
	private final InventoryTransactionRepository inventoryTransactionRepository;
	
	

	public DashboardServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository,
			SupplierRepository supplierRepository, UserRepository userRepository,
			InventoryTransactionRepository inventoryTransactionRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
		this.supplierRepository = supplierRepository;
		this.userRepository = userRepository;
		this.inventoryTransactionRepository = inventoryTransactionRepository;
	}


	@Override
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	public DashboardResponse getDashboard() {
		
		DashboardResponse response = new DashboardResponse();

		response.setTotalProducts(productRepository.count());

		response.setTotalCategories(categoryRepository.count());

		response.setTotalSuppliers(supplierRepository.count());

		response.setTotalUsers(userRepository.count());

		response.setLowStockProducts(
		        productRepository.findLowStockProducts());

		response.setOutOfStockProducts(
		        productRepository.getOutofStockProducts());

		response.setTotalInventoryItems(
		        productRepository.getTotalInventoryItems());

		response.setTotalInventoryValue(
		        productRepository.getTotalInventoryValue());

		return response;
			
			
			
			
	}
	
	
}
