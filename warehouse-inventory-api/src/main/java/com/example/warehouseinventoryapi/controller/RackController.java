package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateRackRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateRackRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.RackResponse;
import com.example.warehouseinventoryapi.service.RackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/racks")
@RequiredArgsConstructor
@Tag(name = "Racks", description = "Gestión de estanterías (racks) dentro de los pasillos")
public class RackController {

    private final RackService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una nueva estantería (rack) en un pasillo")
    public RackResponse create(@Valid @RequestBody CreateRackRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar los datos de una estantería existente por su ID")
    public RackResponse update(@PathVariable Long id, @Valid @RequestBody UpdateRackRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar la información de una estantería por su ID")
    public RackResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una estantería de forma definitiva por su ID")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/aisle/{aisleId}")
    @Operation(summary = "Listar de forma paginada las estanterías pertenecientes a un pasillo específico")
    public PageableResponse<RackResponse> getByAisleId(@PathVariable Long aisleId, Pageable pageable) {
        return service.getByAisleId(aisleId, pageable);
    }
}