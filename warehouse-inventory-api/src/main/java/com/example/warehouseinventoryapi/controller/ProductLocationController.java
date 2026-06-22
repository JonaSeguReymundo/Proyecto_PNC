package com.example.warehouseinventoryapi.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.example.warehouseinventoryapi.dto.request.CreateProductLocationRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.ProductLocationResponse;
import com.example.warehouseinventoryapi.service.ProductLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/product-locations")
@RequiredArgsConstructor
public class ProductLocationController {

    private final ProductLocationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductLocationResponse create(@Valid @RequestBody CreateProductLocationRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public ProductLocationResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/product/{productId}")
    public PageableResponse<ProductLocationResponse> getByProductId(@PathVariable Long productId, Pageable pageable) {
        return service.getByProductId(productId, pageable);
    }

    @GetMapping("/location/{locationId}")
    public PageableResponse<ProductLocationResponse> getByLocationId(@PathVariable Long locationId, Pageable pageable) {
        return service.getByLocationId(locationId, pageable);
    }

    @GetMapping("/exact")
    public ProductLocationResponse getExactRecord(@RequestParam Long productId, @RequestParam Long locationId) {
        return service.getExactRecord(productId, locationId);
    }
}