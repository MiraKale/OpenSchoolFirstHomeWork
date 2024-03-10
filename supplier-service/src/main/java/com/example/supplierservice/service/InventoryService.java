package com.example.supplierservice.service;

import com.example.supplierservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
       // return inventoryRepository.findBySkuCode(skuCode).isPresent();
        return true;
    }
}
