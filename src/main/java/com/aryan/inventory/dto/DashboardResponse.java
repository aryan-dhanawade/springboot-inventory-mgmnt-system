package com.aryan.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

import com.aryan.inventory.entity.Product;

public class DashboardResponse {

	private long totalProducts;

	private long totalCategories;

	private long totalSuppliers;

	private long totalUsers;

	private List<Product> lowStockProducts;

	private List<Product> outOfStockProducts;

	private long totalInventoryItems;

	private BigDecimal totalInventoryValue;

	public DashboardResponse() {
	}

	public long getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(long totalProducts) {
		this.totalProducts = totalProducts;
	}

	public long getTotalCategories() {
		return totalCategories;
	}

	public void setTotalCategories(long totalCategories) {
		this.totalCategories = totalCategories;
	}

	public long getTotalSuppliers() {
		return totalSuppliers;
	}

	public void setTotalSuppliers(long totalSuppliers) {
		this.totalSuppliers = totalSuppliers;
	}

	public long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public List<Product> getLowStockProducts() {
		return lowStockProducts;
	}

	public void setLowStockProducts(List<Product> lowStockProducts) {
		this.lowStockProducts = lowStockProducts;
	}

	public List<Product> getOutOfStockProducts() {
		return outOfStockProducts;
	}

	public void setOutOfStockProducts(List<Product> outOfStockProducts) {
		this.outOfStockProducts = outOfStockProducts;
	}

	public long getTotalInventoryItems() {
		return totalInventoryItems;
	}

	public void setTotalInventoryItems(long totalInventoryItems) {
		this.totalInventoryItems = totalInventoryItems;
	}

	public BigDecimal getTotalInventoryValue() {
		return totalInventoryValue;
	}

	public void setTotalInventoryValue(BigDecimal totalInventoryValue) {
		this.totalInventoryValue = totalInventoryValue;
	}

}