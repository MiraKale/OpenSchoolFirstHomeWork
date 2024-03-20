package com.example.supplierservice.service;

import com.example.supplierservice.error_handler.RestTemplateException;
import com.example.supplierservice.model.Category;
import com.example.supplierservice.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ConsumerService {
    private final RestTemplate restTemplate;

    private final String urlProducts = "http://supplier-service:8080/api/v1/products";
    private final String urlCategories = "http://supplier-service:8080/api/v1/categories";

    @PostConstruct
    public void init() {
        try {
            try {
                //Получение списка всех продуктов и вывод информации о них.
                List<Product> products = restTemplate.getForObject(urlProducts, List.class);
                log.info("All products: {}", products);
            } catch (RestTemplateException e) {
                log.warn(e.getMessage());
            }

            try {
                //Получение списка всех категорий и вывод информации о них.
                List<Category> categories = restTemplate.getForObject(urlCategories, List.class);
                log.info("All categories: {}", categories);
            } catch (RestTemplateException e) {
                log.warn(e.getMessage());
            }
            try {
                //Возможность добавления нового продукта с указанием категории.
                Product iphone15Product = Product
                        .builder()
                        .name("Iphone 15")
                        .description("Mobile phone Iphone 15 generation glass,white")
                        .price(new BigDecimal("170000.99"))
                        .categoryName("Iphone")
                        .build();
                restTemplate.postForObject(urlProducts, iphone15Product, Void.class);
            } catch (RestTemplateException e) {
                log.warn(e.getMessage());
            }
            try {
                //Возможность обновления информации о продукте.
                Product iphoneProduct = Product
                        .builder()
                        .name("Iphone")
                        .description("Mobile phone Iphone ,white")
                        .price(new BigDecimal("130000.99"))
                        .categoryName("Iphone")
                        .build();
                restTemplate.put(urlProducts + "/" + 2L, iphoneProduct, Void.class);
            } catch (RestTemplateException e) {
                log.warn(e.getMessage());
            }
            try {
                //Возможность удаления продукта.
                restTemplate.delete(urlProducts + "/" + 1L);
            } catch (RestTemplateException e) {
                log.warn(e.getMessage());
            }
            try {
                //Реализовать возможность фильтрации продуктов по различным критериям, таким как цена, категория и т. д.
                String urlWithFilterParams = UriComponentsBuilder.fromUriString(urlProducts)
                        .queryParam("categoryName", "Laptop")
                        .toUriString();

                List<Product> productsWithFilter = restTemplate.getForObject(urlWithFilterParams, List.class);
                log.info("products with filter by categoryName Laptop: {}", productsWithFilter);

                String urlWithPriceFilterParams = UriComponentsBuilder.fromUriString(urlProducts)
                        .queryParam("price", 70000)
                        .toUriString();
                List<Product> productsWithPriceFilter = restTemplate.getForObject(urlWithPriceFilterParams, List.class);
                log.info("products with price greater than 70000: {}", productsWithPriceFilter);
            } catch (RestTemplateException e) {
                log.warn(e.getMessage());
            }
            try {
                //Добавить функциональность для поиска продуктов по их названию или описанию.
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlProducts)
                        .queryParam("nameLike", "Iphone");
                String urlWithParams = builder.toUriString();
                List<Product> productWithName = restTemplate.getForObject(urlWithParams, List.class);
                log.info("products with name containing Iphone : {}", productWithName);
            } catch (RestTemplateException e) {
                log.warn(e.getMessage());
            }
            try {
                //Реализовать пагинацию для списка продуктов.
                String urlWithPaginationParams = UriComponentsBuilder.fromUriString(urlProducts + "/paginated")
                        .queryParam("page", 0)
                        .queryParam("size", 2)
                        .toUriString();

                List<Product> productsWithPagination = restTemplate.getForObject(urlWithPaginationParams, List.class);
                log.info("products with pagination page = 0 and size = 2: {}", productsWithPagination);

            } catch (RestTemplateException e) {
                log.warn(e.getMessage());
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

    }
}
