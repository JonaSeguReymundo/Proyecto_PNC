package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.PurchaseEntryRequest;
import com.example.warehouseinventoryapi.dto.request.OrderExitRequest;
import com.example.warehouseinventoryapi.inventory.FifoAllocator;
import com.example.warehouseinventoryapi.dto.response.BatchResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.MovementResponse;
import com.example.warehouseinventoryapi.dto.response.StockSummaryResponse;
import com.example.warehouseinventoryapi.entity.*;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.InventoryMapper;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.repository.*;
import com.example.warehouseinventoryapi.service.AuditLogService;
import com.example.warehouseinventoryapi.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryBatchRepository batchRepository;
    private final InventoryMovementRepository movementRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryMapper mapper;
    private final PageableMapper pageableMapper;
    private final AuditLogService auditLogService;
    private final FifoAllocator fifoAllocator;

    @Override
    @Transactional
    public BatchResponse registerPurchaseEntry(PurchaseEntryRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + request.productId()));

        Warehouse warehouse = warehouseRepository.findById(request.warehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with id: " + request.warehouseId()));

        // Validacion de coherencia de fechas (si ambas vienen).
        if (request.manufacturingDate() != null && request.expirationDate() != null
                && request.expirationDate().isBefore(request.manufacturingDate())) {
            throw new IllegalArgumentException(
                    "Expiration date cannot be before manufacturing date");
        }

        LocalDateTime now = LocalDateTime.now();

        InventoryBatch batch = InventoryBatch.builder()
                .product(product)
                .warehouse(warehouse)
                .batchCode(request.batchCode())
                .initialQuantity(request.quantity())
                .availableQuantity(request.quantity())
                .unitCost(request.unitCost())
                .manufacturingDate(request.manufacturingDate())
                .expirationDate(request.expirationDate())
                .receivedAt(now)
                .build();

        InventoryBatch savedBatch = batchRepository.save(batch);

        String username = currentUsername();

        InventoryMovement movement = InventoryMovement.builder()
                .batch(savedBatch)
                .product(product)
                .warehouse(warehouse)
                .type(MovementType.ENTRY_PURCHASE)
                .quantity(request.quantity())
                .performedBy(username)
                .createdAt(now)
                .reference(request.reference())
                .build();

        movementRepository.save(movement);

        auditLogService.record(username, "ENTRY_PURCHASE", "inventory_batches",
                "Entrada de " + request.quantity() + " unidades del producto " + product.getSku());

        return mapper.toBatchDto(savedBatch);
    }

    @Override
    @Transactional(readOnly = true)
    public StockSummaryResponse getStock(Long productId, Long warehouseId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        List<InventoryBatch> batches =
                batchRepository.findByProductIdAndWarehouseIdOrderByReceivedAtAsc(productId, warehouseId);

        int totalAvailable = batches.stream()
                .mapToInt(InventoryBatch::getAvailableQuantity)
                .sum();

        return new StockSummaryResponse(
                productId,
                product.getSku(),
                warehouseId,
                totalAvailable,
                batches.size()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getBatches(Long productId, Long warehouseId) {
        List<InventoryBatch> batches =
                batchRepository.findByProductIdAndWarehouseIdOrderByReceivedAtAsc(productId, warehouseId);
        return mapper.toBatchDtoList(batches);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<MovementResponse> getMovements(Long productId, Pageable pageable) {
        Page<InventoryMovement> page =
                movementRepository.findByProductIdOrderByCreatedAtDesc(productId, pageable);
        List<MovementResponse> dtoList = mapper.toMovementDtoList(page.getContent());
        return pageableMapper.toPageableResponse(page, dtoList);
    }

    @Override
    @Transactional
    public List<MovementResponse> registerOrderExit(OrderExitRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + request.productId()));

        Warehouse warehouse = warehouseRepository.findById(request.warehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with id: " + request.warehouseId()));

        List<InventoryBatch> batches = batchRepository
                .findByProductIdAndWarehouseIdAndAvailableQuantityGreaterThanOrderByReceivedAtAsc(
                        request.productId(), request.warehouseId(), 0);

        List<FifoAllocator.Allocation> allocations =
                fifoAllocator.allocate(batches, request.quantity());

        String username = currentUsername();
        LocalDateTime now = LocalDateTime.now();
        List<InventoryMovement> movements = new ArrayList<>();

        for (FifoAllocator.Allocation alloc : allocations) {
            InventoryBatch batch = alloc.batch();
            int taken = alloc.quantityTaken();

            batch.setAvailableQuantity(batch.getAvailableQuantity() - taken);
            batchRepository.save(batch);

            InventoryMovement movement = InventoryMovement.builder()
                    .batch(batch)
                    .product(product)
                    .warehouse(warehouse)
                    .type(MovementType.EXIT_ORDER)
                    .quantity(taken)
                    .performedBy(username)
                    .createdAt(now)
                    .reference(request.reference())
                    .build();

            movements.add(movementRepository.save(movement));
        }

        auditLogService.record(username, "EXIT_ORDER", "inventory_batches",
                "Salida FIFO de " + request.quantity() + " unidades del producto " + product.getSku());

        return mapper.toMovementDtoList(movements);
    }

    private String currentUsername() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            return auth != null ? auth.getName() : "system";
        } catch (Exception e) {
            return "system";
        }
    }
}