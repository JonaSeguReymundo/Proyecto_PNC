package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateLevelRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLevelRequest;
import com.example.warehouseinventoryapi.dto.response.LevelResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface LevelService {
    LevelResponse create(CreateLevelRequest request);
    LevelResponse update(Long id, UpdateLevelRequest request);
    LevelResponse getById(Long id);
    void delete(Long id);
    PageableResponse<LevelResponse> getByRackId(Long rackId, Pageable pageable);
}