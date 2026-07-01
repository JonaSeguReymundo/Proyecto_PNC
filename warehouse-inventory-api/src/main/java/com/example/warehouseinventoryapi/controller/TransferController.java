package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateTransferRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.TransferResponse;
import com.example.warehouseinventoryapi.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@Tag(name = "Transfers", description = "Gestión de transferencias de mercancía entre almacenes")
public class TransferController {

    private final TransferService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar y ejecutar una transferencia de stock entre almacenes")
    public TransferResponse create(@Valid @RequestBody CreateTransferRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar los detalles de una transferencia por su ID")
    public TransferResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Historial de transferencias paginadas asociadas a un producto")
    public PageableResponse<TransferResponse> getByProductId(@PathVariable Long productId, Pageable pageable) {
        return service.getByProductId(productId, pageable);
    }

    @GetMapping("/source-warehouse/{warehouseId}")
    @Operation(summary = "Listar transferencias paginadas despachadas desde un almacén de origen")
    public PageableResponse<TransferResponse> getBySourceWarehouseId(@PathVariable Long warehouseId, Pageable pageable) {
        return service.getBySourceWarehouseId(warehouseId, pageable);
    }

    @GetMapping("/destination-warehouse/{warehouseId}")
    @Operation(summary = "Listar transferencias paginadas recibidas en un almacén de destino")
    public PageableResponse<TransferResponse> getByDestinationWarehouseId(@PathVariable Long warehouseId, Pageable pageable) {
        return service.getByDestinationWarehouseId(warehouseId, pageable);
    }
}