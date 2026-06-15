package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {

    // Used to find all racks associated with a specific aisle.
    List<Rack> findByAisleId(Long aisleId);
}