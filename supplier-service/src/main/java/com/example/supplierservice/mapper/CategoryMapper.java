package com.example.supplierservice.mapper;

import com.example.supplierservice.dto.CategoryDto;
import com.example.supplierservice.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category entity) {
        return CategoryDto
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public Category toEntity(CategoryDto dto) {
        return Category
                .builder()
                .name(dto.getName())
                .build();
    }

    public List<CategoryDto> toDtoList(List<Category> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    public List<Category> toEntityList(List<CategoryDto> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
}
