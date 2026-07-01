package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateProductRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateProductRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.ProductResponse;
import com.example.warehouseinventoryapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Gestión del catálogo maestro de productos")
public class ProductController {

    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar un nuevo producto en el catálogo")
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar las propiedades de un producto existente por su ID")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar los detalles de un producto por su ID maestro")
    public ProductResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar de forma lógica/física un producto del catálogo por su ID")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Buscar un producto específico utilizando su código único SKU")
    public ProductResponse getBySku(@PathVariable String sku) {
        return service.getBySku(sku);
    }

    @GetMapping
    @Operation(summary = "Listar de forma paginada todos los productos activos en el sistema")
    public PageableResponse<ProductResponse> getAllActive(Pageable pageable) {
        return service.getAllActive(pageable);
    }
}