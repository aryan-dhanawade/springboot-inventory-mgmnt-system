package com.aryan.inventory.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.aryan.inventory.entity.Category;
import com.aryan.inventory.exception.CategoryNotFoundException;
import com.aryan.inventory.repository.CategoryRepository;
import com.aryan.inventory.util.CaptializeString;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Category addCategory(Category category) {

    			
        return categoryRepository.save(normalizeName(category));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Category updateCategory(Long id, Category updatedCategory) {

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        existing.setName(updatedCategory.getName());

        return categoryRepository.save(existing);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        categoryRepository.delete(category);
    }
    
    private Category normalizeName(Category category) {
    	String normalizedName = CaptializeString.capitalizeString(category.getName());
    	category.setName(normalizedName);
    	return category;
    }
}