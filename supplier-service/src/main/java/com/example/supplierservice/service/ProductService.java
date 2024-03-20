package com.example.supplierservice.service;

import com.example.supplierservice.dto.ProductDto;
import com.example.supplierservice.exception.IdAlreadyExistException;
import com.example.supplierservice.exception.ResourceNotFoundException;
import com.example.supplierservice.mapper.ProductMapper;
import com.example.supplierservice.model.Category;
import com.example.supplierservice.model.Product;
import com.example.supplierservice.repository.ProductRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.supplierservice.specifications.ProductSpecification.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public List<ProductDto> findAll(String categoryNameFilter, String nameLikeFilter,BigDecimal priceFilter) {

        Specification<Product> filters = Specification
                .where(StringUtils.isBlank(categoryNameFilter) ? null : hasCategoryNameEqual(categoryNameFilter))
                .and(priceFilter==null ? null : greaterThanPrice(priceFilter))
                .and(StringUtils.isBlank(nameLikeFilter) ? null : nameLike(nameLikeFilter));


        List<Product> products = productRepository.findAll(filters);

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


    public Product findProductIfExists(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
    }
    @Transactional
    public List<ProductDto> findAllWithPagination(int page, int size) {
        Pageable paging = PageRequest.of(page, size);

        List<Product> products = productRepository.findAll(paging).getContent();

        return productMapper.toDtoList(products);
    }

}
