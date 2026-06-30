package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateRackRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateRackRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.RackResponse;
import com.example.warehouseinventoryapi.entity.Rack;
import com.example.warehouseinventoryapi.exception.DuplicateResourceException;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.mapper.RackMapper;
import com.example.warehouseinventoryapi.repository.AisleRepository; // Added for parent validation
import com.example.warehouseinventoryapi.repository.RackRepository;
import com.example.warehouseinventoryapi.service.RackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RackServiceImpl implements RackService {

    private final RackRepository  repository;
    private final AisleRepository aisleRepository;
    private final RackMapper      mapper;
    private final PageableMapper  pageableMapper;

    @Override
    @Transactional
    public RackResponse create(CreateRackRequest request) {
        if (!aisleRepository.existsById(request.aisleId())) {
            throw new ResourceNotFoundException("Aisle not found with id: " + request.aisleId());
        }

        if (repository.existsByCodeAndAisleId(request.code(), request.aisleId())) {
            throw new DuplicateResourceException("Rack with code '" + request.code() + "' already exists in this aisle.");
        }

        Rack rack = mapper.toEntityCreate(request);
        return mapper.toDto(repository.save(rack));
    }

    @Override
    @Transactional
    public RackResponse update(Long id, UpdateRackRequest request) {
        Rack existingRack = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rack not found with id: " + id));

        if (!existingRack.getCode().equalsIgnoreCase(request.code()) &&
                repository.existsByCodeAndAisleId(request.code(), existingRack.getAisle().getId())) {
            throw new DuplicateResourceException("Cannot update. Rack code '" + request.code() + "' is already in use within this aisle.");
        }

        mapper.updateEntityFromDto(request, existingRack);
        return mapper.toDto(repository.save(existingRack));
    }

    @Override
    @Transactional(readOnly = true)
    public RackResponse getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Rack not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Rack rack = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rack not found with id: " + id));

        repository.delete(rack);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<RackResponse> getByAisleId(Long aisleId, Pageable pageable) {
        if (!aisleRepository.existsById(aisleId)) {
            throw new ResourceNotFoundException("Aisle not found with id: " + aisleId);
        }

        Page<Rack> pageResult = repository.findByAisleId(aisleId, pageable);
        List<RackResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }
}