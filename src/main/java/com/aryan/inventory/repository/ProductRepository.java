package com.aryan.inventory.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aryan.inventory.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	@Query("SELECT p FROM Product p WHERE p.quantity <= p.reorderLevel")
	List<Product> findLowStockProducts();

	Long countByQuantity(int quantity);

	@Query("""
			SELECT COALESCE(SUM(p.quantity),0)
			FROM Product p
				   """)
	Long getTotalInventoryItems();

	@Query("""
			SELECT COALESCE(SUM(p.price * p.quantity),0)
			FROM Product p
			""")
	BigDecimal getTotalInventoryValue();
	
	@Query("""
			SELECT p FROM Product p WHERE p.quantity = 0
			""")
	List<Product> getOutofStockProducts();

}