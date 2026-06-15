package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateLocationRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLocationRequest;
import com.example.warehouseinventoryapi.dto.response.LocationResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponse create(@Valid @RequestBody CreateLocationRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public LocationResponse update(@PathVariable Long id, @Valid @RequestBody UpdateLocationRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public LocationResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/code/{code}")
    public LocationResponse getByCode(@PathVariable String code) {
        return service.getByCode(code);
    }

    @GetMapping("/occupied")
    public PageableResponse<LocationResponse> getByOccupied(@RequestParam Boolean occupied, Pageable pageable) {
        return service.getByOccupied(occupied, pageable);
    }

    @GetMapping("/level/{levelId}")
    public PageableResponse<LocationResponse> getByLevelId(@PathVariable Long levelId, Pageable pageable) {
        return service.getByLevelId(levelId, pageable);
    }
}
