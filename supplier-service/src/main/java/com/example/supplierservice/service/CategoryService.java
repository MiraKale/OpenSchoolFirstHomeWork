package com.example.supplierservice.service;

import com.example.supplierservice.dto.CategoryDto;
import com.example.supplierservice.exception.IdAlreadyExistException;
import com.example.supplierservice.exception.ResourceNotFoundException;
import com.example.supplierservice.mapper.CategoryMapper;
import com.example.supplierservice.model.Category;
import com.example.supplierservice.model.Product;
import com.example.supplierservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Transactional
    public List<CategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }
    @Transactional
    public void create(CategoryDto categoryDto) {
        if (categoryDto.getId() != null) {
            throw new IdAlreadyExistException("You cannot create an entity with existing id");
        }

        Category category = categoryMapper.toEntity(categoryDto);

        categoryRepository.save(category);

    }
    @Transactional
    public CategoryDto findById(Long id) {
        Category category = findCategoryIfExists(id);
        return categoryMapper.toDto(category);
    }
    @Transactional
    public void update(CategoryDto categoryDto, Long id) {
        Category oldCategory = findCategoryIfExists(id);
        Category category = categoryMapper.toEntity(categoryDto);
        category.setId(oldCategory.getId());
        categoryRepository.save(category);
    }
    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    private Category findCategoryIfExists(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
    }
    public Optional<Category> findCategoryByName(String categoryName){
        return categoryRepository.findByName(categoryName);
    }
}
