package com.aryan.inventory.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.aryan.inventory.dto.ProductFilter;
import com.aryan.inventory.dto.ProductRequest;
import com.aryan.inventory.dto.ProductResponse;

import com.aryan.inventory.service.ProductService;

import jakarta.validation.Valid;

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
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id,
                                 @RequestBody ProductRequest updateRequest) {

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
    public List<ProductResponse> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String supplier,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        ProductFilter filter = new ProductFilter();

        filter.setName(name);
        filter.setCategory(category);
        filter.setSupplier(supplier);
        filter.setMinPrice(minPrice);
        filter.setMaxPrice(maxPrice);

        return productService.searchProducts(filter);
    }
    

}