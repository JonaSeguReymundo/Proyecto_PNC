package com.example.warehouseinventoryapi.dto.response;

import com.example.warehouseinventoryapi.entity.ReservationStatus;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long productId,
        String productSku,
        Long warehouseId,
        Integer quantity,
        ReservationStatus status,
        String reference,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        String createdBy
) {}