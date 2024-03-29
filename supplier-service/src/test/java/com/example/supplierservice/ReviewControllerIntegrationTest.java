package com.example.supplierservice;

import com.example.supplierservice.dto.ReviewDto;
import com.example.supplierservice.enums.Evaluation;
import com.example.supplierservice.model.Product;
import com.example.supplierservice.model.Review;
import com.example.supplierservice.repository.ProductRepository;
import com.example.supplierservice.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = TestConfig.class)
class ReviewControllerIntegrationTest {

    @Autowired
    ReviewRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldGetAllReviews() throws Exception {
        Product product = new Product("iphone 15", BigDecimal.valueOf(120000));
        Set<Review> reviews = Set.of(
                new Review(product, Evaluation.GOOD, LocalDateTime.now()),
                new Review(product, Evaluation.AVERAGE, LocalDateTime.now())
        );
        product.setReviews(reviews);
        productRepository.save(product);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    void shouldCreateReview() throws Exception {
        Product product = new Product("iphone 15", BigDecimal.valueOf(120000));
        Long productId = productRepository.save(product).getId();
        ReviewDto reviewDto = new ReviewDto(productId, "GOOD", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        String reviewDtoJson = objectMapper.writeValueAsString(reviewDto);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/reviews")
                        .content(reviewDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertEquals(1, repository.findAll().size());
    }
}
