package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.InventoryBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long> {

    // Lotes de un producto en un almacen, ordenados del mas antiguo al mas nuevo
    List<InventoryBatch> findByProductIdAndWarehouseIdOrderByReceivedAtAsc(Long productId, Long warehouseId);

    // Solo lotes con stock disponible, mas antiguos primero
    List<InventoryBatch> findByProductIdAndWarehouseIdAndAvailableQuantityGreaterThanOrderByReceivedAtAsc(
            Long productId, Long warehouseId, Integer minQuantity);

    // Todos los lotes de un producto (en cualquier almacen)
    List<InventoryBatch> findByProductId(Long productId);
}