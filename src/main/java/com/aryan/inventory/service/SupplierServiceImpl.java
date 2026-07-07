package com.aryan.inventory.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.aryan.inventory.entity.Category;
import com.aryan.inventory.entity.Supplier;
import com.aryan.inventory.exception.SupplierNotFoundException;
import com.aryan.inventory.repository.SupplierRepository;
import com.aryan.inventory.util.CaptializeString;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Supplier addSupplier(Supplier supplier) {
        return supplierRepository.save(normalizeName(supplier));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {

        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));

        existing.setName(updatedSupplier.getName());
        existing.setEmail(updatedSupplier.getEmail());
        existing.setPhone(updatedSupplier.getPhone());
        existing.setAddress(updatedSupplier.getAddress());

        return supplierRepository.save(existing);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteSupplier(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));
        
       
        	supplierRepository.delete(supplier);
    
        
    }
    
    private Supplier normalizeName(Supplier supplier) {
    	String normalizedName = CaptializeString.capitalizeString(supplier.getName());
    	supplier.setName(normalizedName);
    	return supplier;
    }
}