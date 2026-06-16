package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateAisleRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateAisleRequest;
import com.example.warehouseinventoryapi.dto.response.AisleResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.service.AisleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aisles")
@RequiredArgsConstructor
public class AisleController {

    private final AisleService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AisleResponse create(@Valid @RequestBody CreateAisleRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public AisleResponse update(@PathVariable Long id, @Valid @RequestBody UpdateAisleRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public AisleResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public PageableResponse<AisleResponse> getByWarehouseId(@PathVariable Long warehouseId, Pageable pageable) {
        return service.getByWarehouseId(warehouseId, pageable);
    }
}
