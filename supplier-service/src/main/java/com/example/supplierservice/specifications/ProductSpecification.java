package com.example.supplierservice.specifications;


import com.example.supplierservice.model.Category;
import com.example.supplierservice.model.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> nameLike(String nameLike) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + nameLike + "%");
    }

    public static Specification<Product> greaterThanPrice(BigDecimal price) {
        return (root, query, builder) -> builder.greaterThan(root.get("price"), price);
    }

    public static Specification<Product> hasCategoryNameEqual(String categoryName) {
        return (root, query, builder) -> {
            Join<Category,Product> productCategory = root.join("category");
            return builder.equal(productCategory.get("name"), categoryName);
        };
    }


}