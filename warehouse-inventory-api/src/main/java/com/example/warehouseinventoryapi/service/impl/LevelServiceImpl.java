package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateLevelRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLevelRequest;
import com.example.warehouseinventoryapi.dto.response.LevelResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.entity.Level;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.LevelMapper;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.repository.LevelRepository;
import com.example.warehouseinventoryapi.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final LevelRepository repository;
    private final LevelMapper mapper;
    private final PageableMapper pageableMapper;

    @Override
    @Transactional
    public LevelResponse create(CreateLevelRequest request) {
        Level level = mapper.toEntityCreate(request);
        return mapper.toDto(repository.save(level));
    }

    @Override
    @Transactional
    public LevelResponse update(Long id, UpdateLevelRequest request) {
        Level existingLevel = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found with id: " + id));
        mapper.updateEntityFromDto(request, existingLevel);
        return mapper.toDto(repository.save(existingLevel));
    }

    @Override
    @Transactional(readOnly = true)
    public LevelResponse getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Level not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<LevelResponse> getByRackId(Long rackId, Pageable pageable) {
        Page<Level> pageResult = repository.findByRackId(rackId, pageable);
        List<LevelResponse> dtoList = mapper.toDtoList(pageResult.getContent());
        return pageableMapper.toPageableResponse(pageResult, dtoList);
    }
}