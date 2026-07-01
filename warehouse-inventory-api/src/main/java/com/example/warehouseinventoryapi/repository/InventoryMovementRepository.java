package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.InventoryMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    // Historial de movimientos de un producto, paginado
    Page<InventoryMovement> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);

    // Historial de movimientos de un lote concreto
    Page<InventoryMovement> findByBatchIdOrderByCreatedAtDesc(Long batchId, Pageable pageable);
}