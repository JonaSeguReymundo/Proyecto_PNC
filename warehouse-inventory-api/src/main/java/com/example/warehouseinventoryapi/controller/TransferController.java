package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateTransferRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.TransferResponse;
import com.example.warehouseinventoryapi.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponse create(@Valid @RequestBody CreateTransferRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public TransferResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/product/{productId}")
    public PageableResponse<TransferResponse> getByProductId(@PathVariable Long productId, Pageable pageable) {
        return service.getByProductId(productId, pageable);
    }

    @GetMapping("/source-warehouse/{warehouseId}")
    public PageableResponse<TransferResponse> getBySourceWarehouseId(@PathVariable Long warehouseId, Pageable pageable) {
        return service.getBySourceWarehouseId(warehouseId, pageable);
    }

    @GetMapping("/destination-warehouse/{warehouseId}")
    public PageableResponse<TransferResponse> getByDestinationWarehouseId(@PathVariable Long warehouseId, Pageable pageable) {
        return service.getByDestinationWarehouseId(warehouseId, pageable);
    }
}