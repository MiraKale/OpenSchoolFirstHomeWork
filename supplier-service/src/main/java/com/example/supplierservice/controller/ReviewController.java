package com.example.supplierservice.controller;

import com.example.supplierservice.dto.ProductDto;
import com.example.supplierservice.dto.ReviewDto;
import com.example.supplierservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getAll() {
        return reviewService.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody ReviewDto reviewDto) {
        reviewService.create(reviewDto);
    }

}
