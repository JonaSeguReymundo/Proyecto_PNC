package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.response.BatchResponse;
import com.example.warehouseinventoryapi.dto.response.MovementResponse;
import com.example.warehouseinventoryapi.dto.response.ReservationResponse;
import com.example.warehouseinventoryapi.entity.InventoryBatch;
import com.example.warehouseinventoryapi.entity.InventoryMovement;
import com.example.warehouseinventoryapi.entity.StockReservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryMapper {

    public BatchResponse toBatchDto(InventoryBatch b) {
        return new BatchResponse(
                b.getId(),
                b.getProduct().getId(),
                b.getProduct().getSku(),
                b.getWarehouse().getId(),
                b.getBatchCode(),
                b.getInitialQuantity(),
                b.getAvailableQuantity(),
                b.getUnitCost(),
                b.getManufacturingDate(),
                b.getExpirationDate(),
                b.getReceivedAt()
        );
    }

    public List<BatchResponse> toBatchDtoList(List<InventoryBatch> batches) {
        return batches.stream().map(this::toBatchDto).toList();
    }

    public MovementResponse toMovementDto(InventoryMovement m) {
        return new MovementResponse(
                m.getId(),
                m.getBatch() != null ? m.getBatch().getId() : null,
                m.getProduct().getId(),
                m.getWarehouse().getId(),
                m.getType(),
                m.getQuantity(),
                m.getPerformedBy(),
                m.getCreatedAt(),
                m.getReference()
        );
    }

    public List<MovementResponse> toMovementDtoList(List<InventoryMovement> movements) {
        return movements.stream().map(this::toMovementDto).toList();
    }

    public ReservationResponse toReservationDto(StockReservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getProduct().getId(),
                r.getProduct().getSku(),
                r.getWarehouse().getId(),
                r.getQuantity(),
                r.getStatus(),
                r.getReference(),
                r.getCreatedAt(),
                r.getExpiresAt(),
                r.getCreatedBy()
        );
    }

    public List<ReservationResponse> toReservationDtoList(List<StockReservation> reservations) {
        return reservations.stream().map(this::toReservationDto).toList();
    }
}