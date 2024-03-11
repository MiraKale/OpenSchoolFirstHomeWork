package com.example.supplierservice.repository;

import com.example.supplierservice.model.Product;
import com.example.supplierservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("FROM Review r left join fetch r.product")
    List<Review> findAll();
}
