package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateRackRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateRackRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.RackResponse;
import org.springframework.data.domain.Pageable;

public interface RackService {
    RackResponse create(CreateRackRequest request);
    RackResponse update(Long id, UpdateRackRequest request);
    RackResponse getById(Long id);
    void delete(Long id);
    PageableResponse<RackResponse> getByAisleId(Long aisleId, Pageable pageable);
}