package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    // Useful for listing only active warehouses.
    List<Warehouse> findAllByActiveTrue();

    // Retrieves a paginated list of all active warehouses.
    Page<Warehouse> findAllByActiveTrue(Pageable pageable);

    boolean existsByName(String name);
}