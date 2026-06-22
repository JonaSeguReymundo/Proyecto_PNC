package com.example.warehouseinventoryapi.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.example.warehouseinventoryapi.dto.request.CreateRackRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateRackRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.RackResponse;
import com.example.warehouseinventoryapi.service.RackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/racks")
@RequiredArgsConstructor
public class RackController {

    private final RackService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RackResponse create(@Valid @RequestBody CreateRackRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public RackResponse update(@PathVariable Long id, @Valid @RequestBody UpdateRackRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public RackResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/aisle/{aisleId}")
    public PageableResponse<RackResponse> getByAisleId(@PathVariable Long aisleId, Pageable pageable) {
        return service.getByAisleId(aisleId, pageable);
    }
}