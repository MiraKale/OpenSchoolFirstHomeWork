package com.example.supplierservice.mapper;

import com.example.supplierservice.dto.ProductDto;
import com.example.supplierservice.model.Category;
import com.example.supplierservice.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ProductMapper {

    public ProductDto toDto(Product entity) {
        return ProductDto
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(Objects.requireNonNullElse(entity.getDescription(), ""))
                .price(entity.getPrice())
                .categoryName(Objects.requireNonNullElse(entity.getCategory(), new Category("")).getName())
                .build();
    }

    public Product toEntity(ProductDto dto) {
        return Product
                .builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }

    public List<ProductDto> toDtoList(List<Product> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    public List<Product> toEntityList(List<ProductDto> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
}
