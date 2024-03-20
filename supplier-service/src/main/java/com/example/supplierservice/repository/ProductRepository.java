package com.example.supplierservice.repository;


import com.example.supplierservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("FROM Product p left join fetch p.category")
    List<Product> findAll();

    @Query("FROM Product p left join fetch p.category")
    Page<Product> findAll(Pageable pageable);

    @Query("FROM Product p left join fetch p.category where p.id =:id")
    Optional<Product> findById(Long id);

}
