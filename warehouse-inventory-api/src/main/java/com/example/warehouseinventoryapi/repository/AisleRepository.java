package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.Aisle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AisleRepository extends JpaRepository<Aisle, Long> {

    // Used to find all aisles associated with a specific warehouse.
    List<Aisle> findByWarehouseId(Long warehouseId);

    // Retrieves a paginated list of all aisles belonging to a specific warehouse.
    Page<Aisle> findByWarehouseId(Long warehouseId, Pageable pageable);

    boolean existsByCodeAndWarehouseId(String code, Long warehouseId);
}
