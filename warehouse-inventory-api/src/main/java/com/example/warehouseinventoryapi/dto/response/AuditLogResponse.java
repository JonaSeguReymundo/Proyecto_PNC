package com.example.warehouseinventoryapi.dto.response;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String username,
        String action,
        String affectedTable,
        String detail,
        LocalDateTime createdAt
) {}
