package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateAisleRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateAisleRequest;
import com.example.warehouseinventoryapi.dto.response.AisleResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface AisleService {
    AisleResponse create(CreateAisleRequest request);
    AisleResponse update(Long id, UpdateAisleRequest request);
    AisleResponse getById(Long id);
    void delete(Long id);
    PageableResponse<AisleResponse> getByWarehouseId(Long warehouseId, Pageable pageable);
}