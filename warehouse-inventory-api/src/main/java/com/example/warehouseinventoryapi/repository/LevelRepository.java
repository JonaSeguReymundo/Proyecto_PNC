package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    // Used to find all levels associated with a specific rack.
    List<Level> findByRackId(Long rackId);
}