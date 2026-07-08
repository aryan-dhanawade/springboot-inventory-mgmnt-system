package com.aryan.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.aryan.inventory.dto.ProductFilter;
import com.aryan.inventory.dto.ProductRequest;
import com.aryan.inventory.dto.ProductResponse;
import com.aryan.inventory.entity.Category;
import com.aryan.inventory.entity.Product;
import com.aryan.inventory.entity.Supplier;
import com.aryan.inventory.exception.CategoryNotFoundException;
import com.aryan.inventory.exception.ProductNotFoundException;
import com.aryan.inventory.exception.SupplierNotFoundException;
import com.aryan.inventory.repository.CategoryRepository;
import com.aryan.inventory.repository.ProductRepository;
import com.aryan.inventory.repository.SupplierRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private SupplierRepository supplierRepository;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	void shouldAddProductSuccessfully() {

	    ProductRequest request = createProductRequest();

	    Category category = createCategory();
	    Supplier supplier = createSupplier();
	    Product savedProduct = createProduct(category, supplier);

	    when(categoryRepository.findById(2L))
	            .thenReturn(Optional.of(category));

	    when(supplierRepository.findById(2L))
	            .thenReturn(Optional.of(supplier));

	    when(productRepository.save(any(Product.class)))
	            .thenReturn(savedProduct);

	    ProductResponse response = productService.addProduct(request);

	    assertEquals(1L, response.getId());
	    assertEquals("Laptop", response.getName());
	    assertEquals("Gaming Laptop", response.getDescription());
	    assertEquals(1000, response.getPrice());
	    assertEquals(15, response.getQuantity());
	    assertEquals("LAP001", response.getSku());
	    assertEquals("Electronics", response.getCategory());
	    assertEquals("Dell", response.getSupplier());

	    verify(categoryRepository).findById(2L);
	    verify(supplierRepository).findById(2L);
	    verify(productRepository).save(any(Product.class));
	}
	
	
	
	@Test
	void shouldThrowCategoryNotFoundWhenCategoryDoesNotExist() {

	    ProductRequest request = createProductRequest();

	    when(categoryRepository.findById(2L))
	            .thenReturn(Optional.empty());

	    assertThrows(
	            CategoryNotFoundException.class,
	            () -> productService.addProduct(request)
	    );

	    verify(categoryRepository).findById(2L);
	    verify(supplierRepository, never()).findById(any());
	    verify(productRepository, never()).save(any());
	}
	
	@Test
	void shouldThrowSupplierNotFoundWhenSupplierDoesNotExist() {

	    ProductRequest request = createProductRequest();

	    Category category = createCategory();

	    when(categoryRepository.findById(2L))
	            .thenReturn(Optional.of(category));

	    when(supplierRepository.findById(2L))
	            .thenReturn(Optional.empty());

	    assertThrows(
	            SupplierNotFoundException.class,
	            () -> productService.addProduct(request)
	    );

	    verify(categoryRepository).findById(2L);
	    verify(supplierRepository).findById(2L);
	    verify(productRepository, never()).save(any());
	}
	
	@Test
	void shouldGetProductById() {

	    Category category = createCategory();
	    Supplier supplier = createSupplier();
	    Product product = createProduct(category, supplier);

	    when(productRepository.findById(1L))
	            .thenReturn(Optional.of(product));

	    ProductResponse response = productService.getProductById(1L);

	    assertEquals(1L, response.getId());
	    assertEquals("Laptop", response.getName());
	    assertEquals("Gaming Laptop", response.getDescription());
	    assertEquals(1000, response.getPrice());
	    assertEquals(15, response.getQuantity());
	    assertEquals("LAP001", response.getSku());
	    assertEquals("Electronics", response.getCategory());
	    assertEquals("Dell", response.getSupplier());

	    verify(productRepository).findById(1L);
	}
	
	@Test
	void shouldThrowProductNotFoundWhenProductDoesNotExist() {

	    when(productRepository.findById(1L))
	            .thenReturn(Optional.empty());

	    assertThrows(
	            ProductNotFoundException.class,
	            () -> productService.getProductById(1L)
	    );

	    verify(productRepository).findById(1L);
	}
	
	@Test
	void shouldReturnAllProducts() {

	    Category category = createCategory();
	    Supplier supplier = createSupplier();

	    Product product = createProduct(category, supplier);

	    Pageable pageable = PageRequest.of(0, 10);

	    when(productRepository.findAll(pageable))
	            .thenReturn(new PageImpl<>(List.of(product)));

	    Page<ProductResponse> response =
	            productService.getAllProducts(pageable);

	    assertEquals(1, response.getTotalElements());
	    assertEquals("Laptop", response.getContent().get(0).getName());

	    verify(productRepository).findAll(pageable);
	}
	
	@Test
	void shouldUpdateProductSuccessfully() {

	    ProductRequest request = createProductRequest();

	    Category category = createCategory();
	    Supplier supplier = createSupplier();
	    Product existing = createProduct(category, supplier);

	    when(productRepository.findById(1L))
	            .thenReturn(Optional.of(existing));

	    when(categoryRepository.findById(2L))
	            .thenReturn(Optional.of(category));

	    when(supplierRepository.findById(2L))
	            .thenReturn(Optional.of(supplier));

	    when(productRepository.save(any(Product.class)))
	            .thenReturn(existing);

	    ProductResponse response =
	            productService.updateProduct(1L, request);

	    assertEquals("Laptop", response.getName());
	    assertEquals("Electronics", response.getCategory());
	    assertEquals("Dell", response.getSupplier());

	    verify(productRepository).findById(1L);
	    verify(categoryRepository).findById(2L);
	    verify(supplierRepository).findById(2L);
	    verify(productRepository).save(any(Product.class));
	}
	
	@Test
	void shouldThrowProductNotFoundWhenUpdatingProduct() {

	    ProductRequest request = createProductRequest();

	    when(productRepository.findById(1L))
	            .thenReturn(Optional.empty());

	    assertThrows(
	            ProductNotFoundException.class,
	            () -> productService.updateProduct(1L, request)
	    );

	    verify(productRepository).findById(1L);
	    verify(categoryRepository, never()).findById(any());
	    verify(supplierRepository, never()).findById(any());
	    verify(productRepository, never()).save(any());
	}
	
	@Test
	void shouldThrowCategoryNotFoundWhenUpdatingProduct() {

	    ProductRequest request = createProductRequest();

	    Product existing =
	            createProduct(createCategory(), createSupplier());

	    when(productRepository.findById(1L))
	            .thenReturn(Optional.of(existing));

	    when(categoryRepository.findById(2L))
	            .thenReturn(Optional.empty());

	    assertThrows(
	            CategoryNotFoundException.class,
	            () -> productService.updateProduct(1L, request)
	    );

	    verify(productRepository).findById(1L);
	    verify(categoryRepository).findById(2L);
	    verify(supplierRepository, never()).findById(any());
	    verify(productRepository, never()).save(any());
	}
	
	@Test
	void shouldThrowSupplierNotFoundWhenUpdatingProduct() {

	    ProductRequest request = createProductRequest();

	    Product existing =
	            createProduct(createCategory(), createSupplier());

	    Category category = createCategory();

	    when(productRepository.findById(1L))
	            .thenReturn(Optional.of(existing));

	    when(categoryRepository.findById(2L))
	            .thenReturn(Optional.of(category));

	    when(supplierRepository.findById(2L))
	            .thenReturn(Optional.empty());

	    assertThrows(
	            SupplierNotFoundException.class,
	            () -> productService.updateProduct(1L, request)
	    );

	    verify(productRepository).findById(1L);
	    verify(categoryRepository).findById(2L);
	    verify(supplierRepository).findById(2L);
	    verify(productRepository, never()).save(any());
	}
	
	@Test
	void shouldThrowProductNotFoundWhenDeletingProduct() {

	    when(productRepository.findById(1L))
	            .thenReturn(Optional.empty());

	    assertThrows(
	            ProductNotFoundException.class,
	            () -> productService.deleteProduct(1L)
	    );

	    verify(productRepository).findById(1L);
	    verify(productRepository, never()).delete(any(Product.class));
	}
	
	@Test
	void shouldReturnLowStockProducts() {

	    Product product =
	            createProduct(createCategory(), createSupplier());

	    when(productRepository.findLowStockProducts())
	            .thenReturn(List.of(product));

	    List<ProductResponse> response =
	            productService.getLowStockProducts();

	    assertEquals(1, response.size());
	    assertEquals("Laptop", response.get(0).getName());

	    verify(productRepository).findLowStockProducts();
	}
	
	@Test
	void shouldDeleteProductSuccessfully() {

	    Product product =
	            createProduct(createCategory(), createSupplier());

	    when(productRepository.findById(1L))
	            .thenReturn(Optional.of(product));

	    productService.deleteProduct(1L);

	    verify(productRepository).findById(1L);
	    verify(productRepository).delete(product);
	}
	
	
	@Test
	void shouldSearchProducts() {

	    ProductFilter filter = new ProductFilter();
	    filter.setName("Laptop");

	    Pageable pageable = PageRequest.of(0, 10);

	    Product product =
	            createProduct(createCategory(), createSupplier());

	    when(productRepository.findAll(
	            any(Specification.class),
	            any(Pageable.class)
	    )).thenReturn(new PageImpl<>(List.of(product)));

	    Page<ProductResponse> response =
	            productService.searchProducts(filter, pageable);

	    assertEquals(1, response.getTotalElements());
	    assertEquals("Laptop", response.getContent().get(0).getName());

	    verify(productRepository)
	            .findAll(any(Specification.class), any(Pageable.class));
	}
	
	private ProductRequest createProductRequest() {

	    ProductRequest request = new ProductRequest();

	    request.setName("Laptop");
	    request.setCategoryId(2L);
	    request.setSupplierId(2L);
	    request.setDescription("Gaming Laptop");
	    request.setPrice(1000);
	    request.setSku("LAP001");

	    return request;
	}

	private Category createCategory() {

	    Category category = new Category();

	    category.setId(2L);
	    category.setName("Electronics");

	    return category;
	}

	private Supplier createSupplier() {

	    Supplier supplier = new Supplier();

	    supplier.setId(2L);
	    supplier.setName("Dell");

	    return supplier;
	}

	private Product createProduct(Category category, Supplier supplier) {

	    Product product = new Product();

	    product.setId(1L);
	    product.setName("Laptop");
	    product.setDescription("Gaming Laptop");
	    product.setPrice(1000);
	    product.setQuantity(15);
	    product.setSku("LAP001");
	    product.setCategory(category);
	    product.setSupplier(supplier);

	    return product;
	}
	
	
	


}
