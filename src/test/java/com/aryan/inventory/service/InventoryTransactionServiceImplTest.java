
package com.aryan.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.aryan.inventory.dto.InventoryTransactionRequest;
import com.aryan.inventory.dto.InventoryTransactionResponse;
import com.aryan.inventory.entity.Category;
import com.aryan.inventory.entity.InventoryTransaction;
import com.aryan.inventory.entity.Product;
import com.aryan.inventory.entity.Supplier;
import com.aryan.inventory.entity.TransactionType;
import com.aryan.inventory.entity.User;
import com.aryan.inventory.exception.InsufficientStockException;
import com.aryan.inventory.exception.InternalServerException;
import com.aryan.inventory.exception.ProductNotFoundException;
import com.aryan.inventory.exception.TransactionNotFoundException;
import com.aryan.inventory.repository.InventoryTransactionRepository;
import com.aryan.inventory.repository.ProductRepository;
import com.aryan.inventory.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class InventoryTransactionServiceImplTest {

    @Mock
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InventoryTransactionServiceImpl service;

    
    private void mockAuthenticatedUser() {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);
    }
    
    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldCreatePurchaseTransaction() {
    	
    	mockAuthenticatedUser();
        Product product = createProduct(10);
        User user = createUser();
        InventoryTransactionRequest request = createRequest(TransactionType.PURCHASE, 5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        when(inventoryTransactionRepository.save(any(InventoryTransaction.class)))
                .thenAnswer(i -> {
                    InventoryTransaction t = i.getArgument(0);
                    t.setId(1L);
                    return t;
                });

        InventoryTransactionResponse response = service.createTransaction(request);

        assertEquals(15, product.getQuantity());
        assertEquals(TransactionType.PURCHASE, response.getType());
        verify(productRepository).save(product);
        verify(inventoryTransactionRepository).save(any(InventoryTransaction.class));
    }

    @Test
    void shouldCreateSaleTransaction() {
    	
    	mockAuthenticatedUser();
        Product product = createProduct(10);
        User user = createUser();
        InventoryTransactionRequest request = createRequest(TransactionType.SALE, 4);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        when(inventoryTransactionRepository.save(any(InventoryTransaction.class)))
                .thenAnswer(i -> i.getArgument(0));

        service.createTransaction(request);

        assertEquals(6, product.getQuantity());
    }

    @Test
    void shouldCreateReturnTransaction() {
    	
    	mockAuthenticatedUser();
        Product product = createProduct(10);
        User user = createUser();
        InventoryTransactionRequest request = createRequest(TransactionType.RETURN, 3);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        when(inventoryTransactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.createTransaction(request);

        assertEquals(13, product.getQuantity());
    }

    @Test
    void shouldCreateDamageTransaction() {
    	
    	mockAuthenticatedUser();
        Product product = createProduct(10);
        User user = createUser();
        InventoryTransactionRequest request = createRequest(TransactionType.DAMAGE, 2);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(productRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(inventoryTransactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.createTransaction(request);

        assertEquals(8, product.getQuantity());
    }

    @Test
    void shouldAdjustInventory() {
    	
    	mockAuthenticatedUser();
        Product product = createProduct(100);
        User user = createUser();
        InventoryTransactionRequest request = createRequest(TransactionType.ADJUSTMENT, 25);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(productRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(inventoryTransactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.createTransaction(request);

        assertEquals(25, product.getQuantity());
    }

    @Test
    void shouldThrowProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> service.createTransaction(createRequest(TransactionType.PURCHASE, 5)));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowInternalServerExceptionWhenCurrentUserMissing() {
    	
    	mockAuthenticatedUser();
        when(productRepository.findById(1L)).thenReturn(Optional.of(createProduct(10)));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        assertThrows(InternalServerException.class,
                () -> service.createTransaction(createRequest(TransactionType.PURCHASE, 5)));
    }

    @Test
    void shouldThrowInsufficientStockForSale() {
    	
    	mockAuthenticatedUser();
        when(productRepository.findById(1L)).thenReturn(Optional.of(createProduct(2)));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(createUser()));

        assertThrows(InsufficientStockException.class,
                () -> service.createTransaction(createRequest(TransactionType.SALE, 5)));

        verify(productRepository, never()).save(any(Product.class));
        verify(inventoryTransactionRepository, never()).save(any());
    }

    @Test
    void shouldGetAllTransactions() {
        when(inventoryTransactionRepository.findAll())
                .thenReturn(List.of(createTransaction()));

        assertEquals(1, service.getAllTransactions().size());
    }

    @Test
    void shouldGetTransactionById() {
        when(inventoryTransactionRepository.findById(1L))
                .thenReturn(Optional.of(createTransaction()));

        InventoryTransactionResponse response = service.getTransactionById(1L);

        assertEquals("Laptop", response.getProduct());
    }

    @Test
    void shouldThrowTransactionNotFound() {
        when(inventoryTransactionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class,
                () -> service.getTransactionById(1L));
    }

    @Test
    void shouldGetTransactionsByProduct() {
        when(inventoryTransactionRepository.findByProductId(1L))
                .thenReturn(List.of(createTransaction()));

        assertEquals(1, service.getTransactionsByProduct(1L).size());
    }

    private InventoryTransactionRequest createRequest(TransactionType type, int quantity) {
        InventoryTransactionRequest request = new InventoryTransactionRequest();
        request.setProductId(1L);
        request.setType(type);
        request.setQuantity(quantity);
        request.setRemarks("Test");
        return request;
    }

    private Product createProduct(int quantity) {
        Category c = new Category();
        c.setName("Electronics");

        Supplier s = new Supplier();
        s.setName("Dell");

        Product p = new Product();
        p.setId(1L);
        p.setName("Laptop");
        p.setCategory(c);
        p.setSupplier(s);
        p.setQuantity(quantity);
        return p;
    }

    private User createUser() {
        User u = new User();
        u.setId(1L);
        u.setUsername("admin");
        return u;
    }

    private InventoryTransaction createTransaction() {
        InventoryTransaction t = new InventoryTransaction();
        t.setId(1L);
        t.setProduct(createProduct(10));
        t.setPerformedBy(createUser());
        t.setType(TransactionType.PURCHASE);
        t.setQuantity(5);
        t.setRemarks("Test");
        t.setTimestamp(LocalDateTime.now());
        return t;
    }
}
