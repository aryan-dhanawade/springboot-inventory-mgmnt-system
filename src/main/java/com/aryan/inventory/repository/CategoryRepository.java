package com.aryan.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aryan.inventory.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}