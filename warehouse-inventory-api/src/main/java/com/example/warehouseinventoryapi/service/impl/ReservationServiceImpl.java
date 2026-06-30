package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateReservationRequest;
import com.example.warehouseinventoryapi.dto.response.ReservationResponse;
import com.example.warehouseinventoryapi.entity.*;
import com.example.warehouseinventoryapi.exception.BadRequestException;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.inventory.FifoAllocator;
import com.example.warehouseinventoryapi.mapper.InventoryMapper;
import com.example.warehouseinventoryapi.repository.*;
import com.example.warehouseinventoryapi.service.AuditLogService;
import com.example.warehouseinventoryapi.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);
    private static final int RESERVATION_MINUTES = 15;

    private final StockReservationRepository reservationRepository;
    private final InventoryBatchRepository batchRepository;
    private final InventoryMovementRepository movementRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryMapper mapper;
    private final FifoAllocator fifoAllocator;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public ReservationResponse create(CreateReservationRequest request) {
        if (request.quantity() <= 0) {
            throw new BadRequestException("Reservation quantity must be greater than zero.");
        }

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + request.productId()));

        Warehouse warehouse = warehouseRepository.findById(request.warehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with id: " + request.warehouseId()));

        List<InventoryBatch> batches = batchRepository
                .findByProductIdAndWarehouseIdAndAvailableQuantityGreaterThanOrderByReceivedAtAsc(
                        request.productId(), request.warehouseId(), 0);

        int totalAvailableStock = batches.stream().mapToInt(InventoryBatch::getAvailableQuantity).sum();
        if (totalAvailableStock < request.quantity()) {
            throw new BadRequestException("Insufficient stock. Requested: " + request.quantity() +
                    ", Available: " + totalAvailableStock + " for Product SKU: " + product.getSku());
        }

        List<FifoAllocator.Allocation> allocations =
                fifoAllocator.allocate(batches, request.quantity());

        for (FifoAllocator.Allocation alloc : allocations) {
            InventoryBatch batch = alloc.batch();
            batch.setAvailableQuantity(batch.getAvailableQuantity() - alloc.quantityTaken());
            batchRepository.save(batch);
        }

        LocalDateTime now = LocalDateTime.now();
        String username = currentUsername();

        StockReservation reservation = StockReservation.builder()
                .product(product)
                .warehouse(warehouse)
                .quantity(request.quantity())
                .status(ReservationStatus.ACTIVE)
                .reference(request.reference())
                .createdAt(now)
                .expiresAt(now.plusMinutes(RESERVATION_MINUTES))
                .createdBy(username)
                .build();

        StockReservation saved = reservationRepository.save(reservation);

        movementRepository.save(InventoryMovement.builder()
                .product(product)
                .warehouse(warehouse)
                .type(MovementType.RESERVATION)
                .quantity(request.quantity())
                .performedBy(username)
                .createdAt(now)
                .reference(request.reference())
                .build());

        auditLogService.record(username, "RESERVATION", "stock_reservations",
                "Reserva de " + request.quantity() + " unidades del producto " + product.getSku());

        return mapper.toReservationDto(saved);
    }

    @Override
    @Transactional
    public ReservationResponse confirm(Long id, String reference) {
        StockReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation not found with id: " + id));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new BadRequestException(
                    "Only ACTIVE reservations can be confirmed. Current status: " + reservation.getStatus());
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setReference(reference);
        StockReservation saved = reservationRepository.save(reservation);

        String username = currentUsername();
        movementRepository.save(InventoryMovement.builder()
                .product(reservation.getProduct())
                .warehouse(reservation.getWarehouse())
                .type(MovementType.EXIT_ORDER)
                .quantity(reservation.getQuantity())
                .performedBy(username)
                .createdAt(LocalDateTime.now())
                .reference(reference)
                .build());

        auditLogService.record(username, "CONFIRM_RESERVATION", "stock_reservations",
                "Reserva #" + reservation.getId() + " confirmada");

        return mapper.toReservationDto(saved);
    }

    @Override
    @Transactional
    public ReservationResponse release(Long id) {
        StockReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation not found with id: " + id));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new BadRequestException(
                    "Only ACTIVE reservations can be released. Current status: " + reservation.getStatus());
        }

        returnStockToBatches(reservation);
        reservation.setStatus(ReservationStatus.RELEASED);
        StockReservation saved = reservationRepository.save(reservation);

        String username = currentUsername();
        auditLogService.record(username, "RELEASE_RESERVATION", "stock_reservations",
                "Reserva #" + reservation.getId() + " liberada manualmente");

        return mapper.toReservationDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getById(Long id) {
        return reservationRepository.findById(id).map(mapper::toReservationDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getActive() {
        return mapper.toReservationDtoList(
                reservationRepository.findByStatus(ReservationStatus.ACTIVE));
    }

    @Override
    @Scheduled(fixedRate = 60_000)
    @Transactional
    public int releaseExpired() {
        List<StockReservation> expired = reservationRepository
                .findByStatusAndExpiresAtBefore(ReservationStatus.ACTIVE, LocalDateTime.now());

        for (StockReservation reservation : expired) {
            returnStockToBatches(reservation);
            reservation.setStatus(ReservationStatus.EXPIRED);
            reservationRepository.save(reservation);

            auditLogService.record("system", "EXPIRE_RESERVATION", "stock_reservations",
                    "Reserva #" + reservation.getId() + " expirada automaticamente");
        }

        if (!expired.isEmpty()) {
            log.info("Liberadas {} reservas vencidas", expired.size());
        }
        return expired.size();
    }

    private void returnStockToBatches(StockReservation reservation) {
        List<InventoryBatch> batches = batchRepository
                .findByProductIdAndWarehouseIdOrderByReceivedAtAsc(
                        reservation.getProduct().getId(), reservation.getWarehouse().getId());

        int toReturn = reservation.getQuantity();
        for (InventoryBatch batch : batches) {
            if (toReturn <= 0) break;
            int space = batch.getInitialQuantity() - batch.getAvailableQuantity();
            if (space <= 0) continue;
            int give = Math.min(space, toReturn);
            batch.setAvailableQuantity(batch.getAvailableQuantity() + give);
            batchRepository.save(batch);
            toReturn -= give;
        }
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