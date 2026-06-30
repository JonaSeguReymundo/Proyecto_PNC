package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateAisleRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateAisleRequest;
import com.example.warehouseinventoryapi.dto.response.AisleResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.service.AisleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aisles")
@RequiredArgsConstructor
@Tag(name = "Aisles", description = "Gestión de pasillos de almacenamiento en los almacenes")
public class AisleController {

    private final AisleService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un nuevo pasillo en un almacén")
    public AisleResponse create(@Valid @RequestBody CreateAisleRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar los datos de un pasillo existente por su ID")
    public AisleResponse update(@PathVariable Long id, @Valid @RequestBody UpdateAisleRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar la información de un pasillo por su ID")
    public AisleResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar de forma definitiva un pasillo por su ID")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/warehouse/{warehouseId}")
    @Operation(summary = "Listar de forma paginada todos los pasillos pertenecientes a un almacén específico")
    public PageableResponse<AisleResponse> getByWarehouseId(@PathVariable Long warehouseId, Pageable pageable) {
        return service.getByWarehouseId(warehouseId, pageable);
    }
}