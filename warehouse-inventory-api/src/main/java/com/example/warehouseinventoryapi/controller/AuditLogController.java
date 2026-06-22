package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.response.AuditLogResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Audit Log", description = "Consulta del registro de auditoría")
@SecurityRequirement(name = "bearerAuth")
public class AuditLogController {

    private final AuditLogService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'JEFE_ALMACEN')")
    @Operation(summary = "Todos los registros de auditoría")
    public PageableResponse<AuditLogResponse> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'JEFE_ALMACEN')")
    @Operation(summary = "Registros por usuario")
    public PageableResponse<AuditLogResponse> getByUsername(
            @PathVariable String username, Pageable pageable) {
        return service.getByUsername(username, pageable);
    }

    @GetMapping("/table/{table}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'JEFE_ALMACEN')")
    @Operation(summary = "Registros por tabla afectada")
    public PageableResponse<AuditLogResponse> getByTable(
            @PathVariable String table, Pageable pageable) {
        return service.getByTable(table, pageable);
    }
}
