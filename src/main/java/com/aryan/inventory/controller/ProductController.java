package com.aryan.inventory.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.aryan.inventory.dto.ProductFilter;
import com.aryan.inventory.dto.ProductRequest;
import com.aryan.inventory.dto.ProductResponse;

import com.aryan.inventory.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ProductResponse addProduct(@RequestBody @Valid ProductRequest request) {
		return productService.addProduct(request);
	}

	@GetMapping
	public Page<ProductResponse> getAllProducts(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
			@RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
			@RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "asc") String direction) {

		Pageable pageable = createPageable(page, size, sort, direction);
		;

		return productService.getAllProducts(pageable);
	}

	@GetMapping("/{id}")
	public ProductResponse getProduct(@PathVariable Long id) {
		return productService.getProductById(id);
	}

	@PutMapping("/{id}")
	public ProductResponse updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest updateRequest) {

		return productService.updateProduct(id, updateRequest);
	}

	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}

	@GetMapping("/low-stock")
	public List<ProductResponse> getLowStockProducts() {
		return productService.getLowStockProducts();

	}

	@GetMapping("/search")
	public Page<ProductResponse> searchProducts(@RequestParam(required = false) String name,
			@RequestParam(required = false) String category, @RequestParam(required = false) String supplier,
			@RequestParam(required = false) BigDecimal minPrice, @RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(defaultValue = "0") @PositiveOrZero int page,
			@RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
			@RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "asc") String direction) {

		ProductFilter filter = new ProductFilter();

		Pageable pageable = createPageable(page, size, sort, direction);

		filter.setName(name);
		filter.setCategory(category);
		filter.setSupplier(supplier);
		filter.setMinPrice(minPrice);
		filter.setMaxPrice(maxPrice);

		return productService.searchProducts(filter, pageable);
	}

	private Pageable createPageable(int page, int size, String sort, String direction) {

		Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Set<String> allowed = Set.of(
				"id", 
				"name", 
				"price", 
				"quantity", 
				"category", 
				"supplier"
				);
		
		if(!allowed.contains(sort)) {
			sort = "id";
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

		return pageable;
	}

}