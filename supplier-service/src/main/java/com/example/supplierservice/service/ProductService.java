package com.example.supplierservice.service;

import com.example.supplierservice.dto.ProductDto;
import com.example.supplierservice.exception.IdAlreadyExistException;
import com.example.supplierservice.exception.ResourceNotFoundException;
import com.example.supplierservice.mapper.ProductMapper;
import com.example.supplierservice.model.Category;
import com.example.supplierservice.model.Product;
import com.example.supplierservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDtoList(products);
    }

    @Transactional
    public void create(ProductDto productDto) {
        if (productDto.getId() != null) {
            throw new IdAlreadyExistException("You cannot create an entity with existing id");
        }
        Product product = productMapper.toEntity(productDto);

        addCategoryIfExist(productDto, product);
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Product product = findProductIfExists(id);

        return productMapper.toDto(product);
    }

    @Transactional
    public void update(ProductDto productDto, Long id) {
        Product oldProduct = findProductIfExists(id);
        Product product = productMapper.toEntity(productDto);
        product.setId(oldProduct.getId());

        addCategoryIfExist(productDto, product);
    }

    private void addCategoryIfExist(ProductDto productDto, Product product) {
        String categoryName = productDto.getCategoryName();
        if (categoryName != null) {
            Optional<Category> categoryOptional = categoryService.findCategoryByName(categoryName);

            Category category = categoryOptional.orElseGet(() -> new Category(categoryName));

            product.setCategory(category);
        }

        productRepository.save(product);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProductDto findByName(String name) {
        Product product = productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Not Found with name: " + name));
        return productMapper.toDto(product);
    }

    public Product findProductIfExists(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
    }

    public List<ProductDto> findAllWithPagination(int page, int size) {
        Pageable paging = PageRequest.of(page, size);

        List<Product> products = productRepository.findAll(paging).getContent();

        return productMapper.toDtoList(products);
    }

    public List<ProductDto> findAllWithFilter(String categoryName, BigDecimal price) {
        List<Product> products = new ArrayList<>();

        if (categoryName != null) {
            products = productRepository.findByCategoryNameEquals(categoryName);
        } else if (price != null) {
            products = productRepository.findByCategoryAndPriceGreaterThan(price);
        }

        return productMapper.toDtoList(products);
    }
}
