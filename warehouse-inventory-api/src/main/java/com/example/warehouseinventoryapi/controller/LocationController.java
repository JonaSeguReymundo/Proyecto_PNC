package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateLocationRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLocationRequest;
import com.example.warehouseinventoryapi.dto.response.LocationResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Tag(name = "Locations", description = "Gestión de ubicaciones físicas (slots) de almacenamiento")
public class LocationController {

    private final LocationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una nueva ubicación de almacenamiento")
    public LocationResponse create(@Valid @RequestBody CreateLocationRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar los datos de una ubicación existente por su ID")
    public LocationResponse update(@PathVariable Long id, @Valid @RequestBody UpdateLocationRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar la información de una ubicación por su ID")
    public LocationResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una ubicación de forma definitiva por su ID")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Buscar una ubicación específica utilizando su código único de barra/etiqueta")
    public LocationResponse getByCode(@PathVariable String code) {
        return service.getByCode(code);
    }

    @GetMapping("/occupied")
    @Operation(summary = "Filtrar y listar de forma paginada las ubicaciones según su estado de ocupación")
    public PageableResponse<LocationResponse> getByOccupied(
            @RequestParam(required = false, defaultValue = "true") Boolean occupied,
            Pageable pageable) {
        return service.getByOccupied(occupied, pageable);
    }

    @GetMapping("/level/{levelId}")
    @Operation(summary = "Listar de forma paginada todas las ubicaciones que pertenecen a un nivel específico")
    public PageableResponse<LocationResponse> getByLevelId(@PathVariable Long levelId, Pageable pageable) {
        return service.getByLevelId(levelId, pageable);
    }
}