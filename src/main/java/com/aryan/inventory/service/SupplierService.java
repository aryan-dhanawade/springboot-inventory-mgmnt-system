package com.aryan.inventory.service;

import java.util.List;

import com.aryan.inventory.entity.Supplier;

public interface SupplierService {

    Supplier addSupplier(Supplier supplier);

    List<Supplier> getAllSuppliers();

    Supplier getSupplierById(Long id);

    Supplier updateSupplier(Long id, Supplier supplier);

    void deleteSupplier(Long id);

}