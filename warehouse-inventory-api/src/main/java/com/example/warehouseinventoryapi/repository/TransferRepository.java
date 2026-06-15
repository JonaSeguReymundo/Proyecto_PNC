package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    // Retrieves the movement history for a specific product.
    List<Transfer> findByProductId(Long productId);

    // Retrieves the outbound movement history from a specific warehouse.
    List<Transfer> findBySourceWarehouseId(Long warehouseId);

    // Retrieves the inbound movement history into a specific warehouse.
    List<Transfer> findByDestinationWarehouseId(Long warehouseId);
}
