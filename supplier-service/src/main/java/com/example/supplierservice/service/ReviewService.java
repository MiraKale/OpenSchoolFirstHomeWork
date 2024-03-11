package com.example.supplierservice.service;

import com.example.supplierservice.dto.ProductDto;
import com.example.supplierservice.dto.ReviewDto;
import com.example.supplierservice.exception.IdAlreadyExistException;
import com.example.supplierservice.mapper.ProductMapper;
import com.example.supplierservice.mapper.ReviewMapper;
import com.example.supplierservice.model.Product;
import com.example.supplierservice.model.Review;
import com.example.supplierservice.repository.ProductRepository;
import com.example.supplierservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    private final ProductService productService;
    @Transactional(readOnly = true)
    public List<ReviewDto> findAll() {
        List<Review> reviews = reviewRepository.findAll();
        return reviewMapper.toDtoList(reviews);
    }
    @Transactional
    public void create(ReviewDto reviewDto) {
        if (reviewDto.getId() != null) {
            throw new IdAlreadyExistException("You cannot create an entity with existing id");
        }
        Product product = productService.findProductIfExists(reviewDto.getProductId());
        Review review = reviewMapper.toEntity(reviewDto, product);
        reviewRepository.save(review);
    }
}
