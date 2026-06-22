package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateLocationRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLocationRequest;
import com.example.warehouseinventoryapi.dto.response.LocationResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface LocationService {
    LocationResponse create(CreateLocationRequest request);
    LocationResponse update(Long id, UpdateLocationRequest request);
    LocationResponse getById(Long id);
    void delete(Long id);
    LocationResponse getByCode(String code);
    PageableResponse<LocationResponse> getByOccupied(Boolean occupied, Pageable pageable);
    PageableResponse<LocationResponse> getByLevelId(Long levelId, Pageable pageable);
}