package com.aryan.inventory.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aryan.inventory.dto.ProductFilter;
import com.aryan.inventory.dto.ProductRequest;
import com.aryan.inventory.dto.ProductResponse;

public interface ProductService {

	ProductResponse addProduct(ProductRequest request);

	ProductResponse getProductById(Long id);

	Page<ProductResponse> getAllProducts(Pageable pageable);

	ProductResponse updateProduct(Long id, ProductRequest request);

	void deleteProduct(Long id);

	List<ProductResponse> getLowStockProducts();

	Page<ProductResponse> searchProducts(ProductFilter filter, Pageable pageable);

}
