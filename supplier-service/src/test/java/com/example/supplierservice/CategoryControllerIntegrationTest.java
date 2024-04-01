package com.example.supplierservice;

import com.example.supplierservice.dto.CategoryDto;
import com.example.supplierservice.model.Category;
import com.example.supplierservice.repository.CategoryRepository;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = TestConfig.class)
class CategoryControllerIntegrationTest {

    @Autowired
    CategoryRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldGetAllCategories() throws Exception {
        List<Category> categories = List.of(
                new Category("phone"),
                new Category("laptop")
        );
        repository.saveAll(categories);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldGetCategoryById() throws Exception {

        Category category = new Category("phone");


        repository.save(category);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("phone"));
    }

    @Test
    void shouldGetException_thenCategoryByIdNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto("phone");
        String categoryDtoJson = objectMapper.writeValueAsString(categoryDto);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/categories")
                        .content(categoryDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldGetException_thenCategoryToCreateAlreadyHaveId() throws Exception {
        CategoryDto categoryDto = new CategoryDto(1L, "phone");
        String categoryDtoJson = objectMapper.writeValueAsString(categoryDto);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/categories")
                        .content(categoryDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Category category = new Category("phone");
        Category categoryAfterSaved = repository.save(category);
        CategoryDto categoryDto = new CategoryDto("laptop");
        String categoryJson = objectMapper.writeValueAsString(categoryDto);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/categories/{id}", categoryAfterSaved.getId())
                        .content(categoryJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, repository.findAll().size());
        assertEquals("laptop", repository.findById(categoryAfterSaved.getId()).get().getName());
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        Category category = new Category("phone");
        repository.save(category);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }
}
