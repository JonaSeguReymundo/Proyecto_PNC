package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateProductLocationRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.ProductLocationResponse;
import com.example.warehouseinventoryapi.service.ProductLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-locations")
@RequiredArgsConstructor
@Tag(name = "Product Locations", description = "Asignación y mapeo de productos en ubicaciones físicas")
public class ProductLocationController {

    private final ProductLocationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Asignar un producto a una ubicación específica")
    public ProductLocationResponse create(@Valid @RequestBody CreateProductLocationRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un registro de ubicación de producto por su ID")
    public ProductLocationResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover la asignación de un producto de una ubicación por su ID")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Listar de forma paginada todas las ubicaciones donde se encuentra un producto")
    public PageableResponse<ProductLocationResponse> getByProductId(@PathVariable Long productId, Pageable pageable) {
        return service.getByProductId(productId, pageable);
    }

    @GetMapping("/location/{locationId}")
    @Operation(summary = "Listar de forma paginada los productos contenidos en una ubicación específica")
    public PageableResponse<ProductLocationResponse> getByLocationId(@PathVariable Long locationId, Pageable pageable) {
        return service.getByLocationId(locationId, pageable);
    }

    @GetMapping("/exact")
    @Operation(summary = "Obtener el registro exacto de la relación entre un producto y una ubicación")
    public ProductLocationResponse getExactRecord(@RequestParam Long productId, @RequestParam Long locationId) {
        return service.getExactRecord(productId, locationId);
    }
}