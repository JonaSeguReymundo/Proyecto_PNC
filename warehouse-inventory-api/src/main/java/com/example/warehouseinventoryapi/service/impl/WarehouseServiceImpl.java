package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateWarehouseRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateWarehouseRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.WarehouseResponse;
import com.example.warehouseinventoryapi.entity.Warehouse;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.mapper.WarehouseMapper;
import com.example.warehouseinventoryapi.repository.WarehouseRepository;
import com.example.warehouseinventoryapi.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional
    public WarehouseResponse create(CreateWarehouseRequest request) {
        Warehouse warehouse = mapper.toEntityCreate(request);
        Warehouse savedWarehouse = repository.save(warehouse);
        return mapper.toDto(savedWarehouse);
    }

    @Override
    @Transactional
    public WarehouseResponse update(Long id, UpdateWarehouseRequest request) {
        Warehouse existingWarehouse = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        mapper.updateEntityFromDto(request, existingWarehouse);

        Warehouse updatedWarehouse = repository.save(existingWarehouse);
        return mapper.toDto(updatedWarehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<WarehouseResponse> getAllActive(Pageable pageable) {
        Page<Warehouse> warehousePage = repository.findAllByActiveTrue(pageable);

        List<WarehouseResponse> dtoList = mapper.toDtoList(warehousePage.getContent());

        return pageableMapper.toPageableResponse(warehousePage, dtoList);
    }
}