package com.example.productservice.repositories;

import com.example.productservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long aLong);
    List<Product> findByTitleContaining(String query);

    Page<Product> findAllByTitleContainingAndCategory_Id(
            String title, Long categoryId, Pageable pageable
    );
}
