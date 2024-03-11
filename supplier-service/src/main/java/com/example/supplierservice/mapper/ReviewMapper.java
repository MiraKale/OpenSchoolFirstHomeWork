package com.example.supplierservice.mapper;

import com.example.supplierservice.dto.ReviewDto;
import com.example.supplierservice.enums.Evaluation;
import com.example.supplierservice.model.Product;
import com.example.supplierservice.model.Review;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Component
public class ReviewMapper {
    public ReviewDto toDto(Review entity) {
        return ReviewDto
                .builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .comment(Objects.requireNonNullElse(entity.getComment(), ""))
                .evaluation(entity.getEvaluation().name())
                .timestamp(entity.getTimestamp().toEpochSecond(ZoneOffset.UTC))
                .build();
    }

    public Review toEntity(ReviewDto dto, Product product) {
        return Review
                .builder()
                .comment(dto.getComment())
                .evaluation(Evaluation.valueOf(dto.getEvaluation()))
                .timestamp(LocalDateTime.ofEpochSecond(dto.getTimestamp(), 0, ZoneOffset.UTC))
                .product(product)
                .build();
    }

    public List<ReviewDto> toDtoList(List<Review> entities) {
        return entities.stream().map(this::toDto).toList();
    }

}


