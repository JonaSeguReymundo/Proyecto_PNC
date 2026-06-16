package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateLevelRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLevelRequest;
import com.example.warehouseinventoryapi.dto.response.LevelResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.service.LevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/levels")
@RequiredArgsConstructor
public class LevelController {

    private final LevelService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelResponse create(@Valid @RequestBody CreateLevelRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public LevelResponse update(@PathVariable Long id, @Valid @RequestBody UpdateLevelRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public LevelResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/rack/{rackId}")
    public PageableResponse<LevelResponse> getByRackId(@PathVariable Long rackId, Pageable pageable) {
        return service.getByRackId(rackId, pageable);
    }
}