package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.Rack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {

    // Used to find all racks associated with a specific aisle.
    List<Rack> findByAisleId(Long aisleId);

    // Retrieves a paginated list of all racks belonging to a specific aisle.
    Page<Rack> findByAisleId(Long aisleId, Pageable pageable);

    boolean existsByCodeAndAisleId(String code, Long aisleId);
}