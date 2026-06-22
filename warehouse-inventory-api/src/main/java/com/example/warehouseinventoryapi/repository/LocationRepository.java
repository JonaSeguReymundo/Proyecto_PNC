package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // Finds a location by its exact unique code.
    Optional<Location> findByCode(String code);

    // Retrieves locations based on their occupancy status (occupied or empty).
    List<Location> findByOccupied(Boolean occupied);

    // Retrieves all locations belonging to a specific level.
    List<Location> findByLevelId(Long levelId);

    // Retrieves a paginated list of locations based on their occupancy status.
    Page<Location> findByOccupied(Boolean occupied, Pageable pageable);

    // Retrieves a paginated list of all locations belonging to a specific level.
    Page<Location> findByLevelId(Long levelId, Pageable pageable);
}