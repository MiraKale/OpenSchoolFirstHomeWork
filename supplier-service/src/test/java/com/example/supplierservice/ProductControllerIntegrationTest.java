package com.example.supplierservice;

import com.example.supplierservice.dto.ProductDto;
import com.example.supplierservice.model.Product;
import com.example.supplierservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = TestConfig.class)
class ProductControllerIntegrationTest {
    @Autowired
    ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        List<Product> products = List.of(
                new Product("iphone 13", BigDecimal.valueOf(100000)),
                new Product("iphone 14", BigDecimal.valueOf(110000)),
                new Product("iphone 15", BigDecimal.valueOf(120000))
        );
        repository.saveAll(products);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void shouldGetAllProducts_withFilterByPrice() throws Exception {
        List<Product> products = List.of(
                new Product("iphone 13", BigDecimal.valueOf(100000)),
                new Product("iphone 14", BigDecimal.valueOf(110000)),
                new Product("iphone 15", BigDecimal.valueOf(120000))
        );
        repository.saveAll(products);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/products?price=105000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldGetProductById() throws Exception {

        Product product = new Product("iphone 13", BigDecimal.valueOf(100000));

        repository.save(product);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("iphone 13"));
    }

    @Test
    void shouldGetException_thenProductByIdNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductDto productDto = new ProductDto("iphone 13", BigDecimal.valueOf(100000));
        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/products")
                        .content(productDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldGetException_thenProductToCreateAlreadyHaveId() throws Exception {
        ProductDto productDto = new ProductDto(1L,"iphone 13","description", BigDecimal.valueOf(100000),"category");
        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .content(productDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        Product product = new Product("iphone 13", BigDecimal.valueOf(100000));
        repository.save(product);
        ProductDto productDto = new ProductDto("iphone 14", BigDecimal.valueOf(100000));
        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/products/{id}", product.getId())
                        .content(productDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, repository.findAll().size());
        assertEquals("iphone 14", repository.findById(product.getId()).get().getName());
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product product = new Product("iphone 13", BigDecimal.valueOf(100000));
        repository.save(product);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldGetOneProducts_withPagination_with0page1size() throws Exception {
        List<Product> products = List.of(
                new Product("iphone 13", BigDecimal.valueOf(100000)),
                new Product("iphone 14", BigDecimal.valueOf(110000)),
                new Product("iphone 15", BigDecimal.valueOf(120000))
        );
        repository.saveAll(products);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/products/paginated?page=0&size=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
