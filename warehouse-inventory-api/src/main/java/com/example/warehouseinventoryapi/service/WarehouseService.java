package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateWarehouseRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateWarehouseRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.WarehouseResponse;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {
    WarehouseResponse create(CreateWarehouseRequest request);
    WarehouseResponse update(Long id, UpdateWarehouseRequest request);
    WarehouseResponse getById(Long id);
    void delete(Long id);
    PageableResponse<WarehouseResponse> getAllActive(Pageable pageable);
}