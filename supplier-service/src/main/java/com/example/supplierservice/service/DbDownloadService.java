package com.example.supplierservice.service;

import com.example.supplierservice.model.Category;
import com.example.supplierservice.model.Product;
import com.example.supplierservice.repository.CategoryRepository;
import com.example.supplierservice.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class DbDownloadService {
    private final ProductRepository productRepository;

  // @PostConstruct
    public void init() {
        Category iphoneCategory= Category
                .builder()
                .name("Iphone")
                .build();
        Category laptopCategory = Category
                .builder()
                .name("Laptop")
                .build();

        Product iphone14Product = Product
                .builder()
                .name("Iphone 14")
                .description("Mobile phone Iphone 14 generation,red")
                .price(new BigDecimal("123333.99"))
                .category(iphoneCategory)
                .build();
        Product iphone12Product = Product
                .builder()
                .name("Iphone 12")
                .description("Mobile phone Iphone 12 generation,grey")
                .price(new BigDecimal("100000"))
                .category(iphoneCategory)
                .build();
        Product honorMagicbookProduct = Product
                .builder()
                .name("Honor Magicbook 14")
                .description("Laptop Honor Magicbook 14,black")
                .price(new BigDecimal("50000.59"))
                .category(laptopCategory)
                .build();

        List<Product> products = List.of(iphone12Product,iphone14Product,honorMagicbookProduct);
        productRepository.saveAll(products);
    }
}
