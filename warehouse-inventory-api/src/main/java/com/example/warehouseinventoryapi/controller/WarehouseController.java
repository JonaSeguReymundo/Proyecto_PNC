package com.example.warehouseinventoryapi.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.example.warehouseinventoryapi.dto.request.CreateWarehouseRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateWarehouseRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.WarehouseResponse;
import com.example.warehouseinventoryapi.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WarehouseResponse create(@Valid @RequestBody CreateWarehouseRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public WarehouseResponse update(@PathVariable Long id, @Valid @RequestBody UpdateWarehouseRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public WarehouseResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public PageableResponse<WarehouseResponse> getAllActive(Pageable pageable) {
        return service.getAllActive(pageable);
    }
}