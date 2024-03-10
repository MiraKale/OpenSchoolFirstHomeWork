package com.example.supplierservice.controller;

import com.example.supplierservice.model.Category;
import com.example.supplierservice.model.Product;
import com.example.supplierservice.repository.CategoryRepository;
import com.example.supplierservice.repository.ProductRepository;
import com.example.supplierservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductRepository repository;
    private final CategoryRepository crepository;

    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        Product next = repository.findAll().iterator().next();
        Category category = crepository.findAll().iterator().next();
        next.setName("3");
        category.setName("3");
        repository.save(next);
        return "";
    }
}
