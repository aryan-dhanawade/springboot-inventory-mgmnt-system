package com.aryan.inventory.service;


import java.util.List;


import com.aryan.inventory.dto.ProductRequest;
import com.aryan.inventory.dto.ProductResponse;


public interface ProductService {
	
	ProductResponse addProduct(ProductRequest request);

	ProductResponse getProductById(Long id);

	List<ProductResponse> getAllProducts();

	ProductResponse updateProduct(Long id, ProductRequest request);
	
	void deleteProduct(Long id);
	
	List<ProductResponse> getLowStockProducts();
	
	
	

}
