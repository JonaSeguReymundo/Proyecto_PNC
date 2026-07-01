package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.ProductLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductLocationRepository extends JpaRepository<ProductLocation, Long> {

    // Finds all physical locations where a specific product is stored.
    List<ProductLocation> findByProductId(Long productId);

    // Retrieves all products currently stored in a specific location.
    List<ProductLocation> findByLocationId(Long locationId);

    // Fetches the exact stock record for a product in a specific location (critical for transfers and stock intake).
    Optional<ProductLocation> findByProductIdAndLocationId(Long productId, Long locationId);

    // Finds all physical locations where a specific product is stored with pagination.
    Page<ProductLocation> findByProductId(Long productId, Pageable pageable);

    // Retrieves all products currently stored in a specific location with pagination.
    Page<ProductLocation> findByLocationId(Long locationId, Pageable pageable);

    boolean existsByProductIdAndLocationId(Long productId, Long locationId);
}
