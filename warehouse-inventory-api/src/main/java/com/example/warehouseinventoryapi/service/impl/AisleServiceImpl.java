package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateAisleRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateAisleRequest;
import com.example.warehouseinventoryapi.dto.response.AisleResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.entity.Aisle;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.AisleMapper;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.repository.AisleRepository;
import com.example.warehouseinventoryapi.service.AisleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AisleServiceImpl implements AisleService {

    private final AisleRepository repository;
    private final AisleMapper mapper;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional
    public AisleResponse create(CreateAisleRequest request) {
        Aisle aisle = mapper.toEntityCreate(request);
        return mapper.toDto(repository.save(aisle));
    }

    @Override
    @Transactional
    public AisleResponse update(Long id, UpdateAisleRequest request) {
        Aisle existingAisle = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aisle not found with id: " + id));
        mapper.updateEntityFromDto(request, existingAisle);
        return mapper.toDto(repository.save(existingAisle));
    }

    @Override
    @Transactional(readOnly = true)
    public AisleResponse getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Aisle not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Aisle not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<AisleResponse> getByWarehouseId(Long warehouseId, Pageable pageable) {
        Page<Aisle> pageResult = repository.findByWarehouseId(warehouseId, pageable);
        List<AisleResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }
}