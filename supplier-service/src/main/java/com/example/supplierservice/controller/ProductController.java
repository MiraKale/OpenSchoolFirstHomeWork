package com.example.supplierservice.controller;

import com.example.supplierservice.dto.ProductDto;
import com.example.supplierservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAll(@RequestParam(required = false) String categoryName,
                                   @RequestParam(required = false) String nameLike,
                                   @RequestParam(required = false) BigDecimal price) {
        return productService.findAll(categoryName, nameLike,price);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody ProductDto productDto) {
        productService.create(productDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto show(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody @Valid ProductDto productDto, @PathVariable Long id) {
        productService.update(productDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteById(id);
    }

    @GetMapping("/paginated")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllWithPagination(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "3") int size) {
        return productService.findAllWithPagination(page, size);
    }
}
