package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Essential for preventing duplicate products and fetching items via barcode scanning.
    Optional<Product> findBySku(String sku);

    // Used for user-facing catalogs or active product listings.
    List<Product> findAllByActiveTrue();

    // Used for user-facing catalogs or active product listings with pagination.
    Page<Product> findAllByActiveTrue(Pageable pageable);
}
