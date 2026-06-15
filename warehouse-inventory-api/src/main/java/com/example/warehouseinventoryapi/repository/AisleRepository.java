package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.Aisle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AisleRepository extends JpaRepository<Aisle, Long> {

    // Used to find all aisles associated with a specific warehouse.
    List<Aisle> findByWarehouseId(Long warehouseId);
}
