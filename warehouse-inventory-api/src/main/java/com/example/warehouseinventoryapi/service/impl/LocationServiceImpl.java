package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateLocationRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLocationRequest;
import com.example.warehouseinventoryapi.dto.response.LocationResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.entity.Location;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.LocationMapper;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.repository.LocationRepository;
import com.example.warehouseinventoryapi.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository repository;
    private final LocationMapper mapper;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional
    public LocationResponse create(CreateLocationRequest request) {
        Location location = mapper.toEntityCreate(request);
        return mapper.toDto(repository.save(location));
    }

    @Override
    @Transactional
    public LocationResponse update(Long id, UpdateLocationRequest request) {
        Location existingLocation = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        mapper.updateEntityFromDto(request, existingLocation);
        return mapper.toDto(repository.save(existingLocation));
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponse getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Location not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponse getByCode(String code) {
        return repository.findByCode(code).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with code: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<LocationResponse> getByOccupied(Boolean occupied, Pageable pageable) {
        Page<Location> pageResult = repository.findByOccupied(occupied, pageable);
        List<LocationResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<LocationResponse> getByLevelId(Long levelId, Pageable pageable) {
        Page<Location> pageResult = repository.findByLevelId(levelId, pageable);
        List<LocationResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }
}