package com.aryan.inventory.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.aryan.inventory.dto.ProductFilter;
import com.aryan.inventory.dto.ProductRequest;
import com.aryan.inventory.dto.ProductResponse;

import com.aryan.inventory.entity.Category;
import com.aryan.inventory.entity.Product;
import com.aryan.inventory.entity.Supplier;

import com.aryan.inventory.repository.CategoryRepository;
import com.aryan.inventory.repository.ProductRepository;
import com.aryan.inventory.repository.SupplierRepository;
import com.aryan.inventory.specification.ProductSpecification;
import com.aryan.inventory.exception.CategoryNotFoundException;
import com.aryan.inventory.exception.SupplierNotFoundException;
import com.aryan.inventory.exception.ProductNotFoundException;

@Service

public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final SupplierRepository supplierRepository;

	public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
			SupplierRepository supplierRepository) {

		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.supplierRepository = supplierRepository;
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ProductResponse addProduct(ProductRequest request) {

		Category category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

		Supplier supplier = supplierRepository.findById(request.getSupplierId())
				.orElseThrow(() -> new SupplierNotFoundException(request.getSupplierId()));

		Product product = new Product();

		mapRequestToProduct(product, request, category, supplier);

		Product saved = productRepository.save(product);

		return mapProductToResponse(saved);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
	public List<ProductResponse> getAllProducts() {

		return productRepository.findAll().stream().map(this::mapProductToResponse).toList();
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
	public ProductResponse getProductById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		return mapProductToResponse(product);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ProductResponse updateProduct(Long id, ProductRequest updateRequest) {

		Product existing = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		Category updatedCategory = categoryRepository.findById(updateRequest.getCategoryId())
				.orElseThrow(() -> new CategoryNotFoundException(updateRequest.getCategoryId()));

		Supplier updatedSupplier = supplierRepository.findById(updateRequest.getSupplierId())
				.orElseThrow(() -> new SupplierNotFoundException(updateRequest.getSupplierId()));

		mapRequestToProduct(existing, updateRequest, updatedCategory, updatedSupplier);

		Product saved = productRepository.save(existing);

		return mapProductToResponse(saved);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN')")
	public void deleteProduct(Long id) {

		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		productRepository.delete(product);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public List<ProductResponse> getLowStockProducts() {

		return productRepository.findLowStockProducts().stream().map(this::mapProductToResponse).toList();

	}
	
	@Override
	@PreAuthorize("isAuthenticated()")
	public List<ProductResponse> searchProducts(ProductFilter filter){
		return productRepository
				.findAll(ProductSpecification.filter(filter))
				.stream()
				.map(this::mapProductToResponse)
				.toList();
	}

	private void mapRequestToProduct(Product product, ProductRequest request, Category category, Supplier supplier) {
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
//		product.setQuantity(request.getQuantity());
		product.setSku(request.getSku());
		product.setCategory(category);
		product.setSupplier(supplier);
		
		
		
	}

	private ProductResponse mapProductToResponse(Product product) {

		ProductResponse response = new ProductResponse();

		response.setId(product.getId());
		response.setName(product.getName());
		response.setDescription(product.getDescription());
		response.setPrice(product.getPrice());
		response.setQuantity(product.getQuantity());
		response.setSku(product.getSku());

		response.setCategory(product.getCategory().getName());
		response.setSupplier(product.getSupplier().getName());

		return response;
	}
}