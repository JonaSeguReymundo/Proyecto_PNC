package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateLevelRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLevelRequest;
import com.example.warehouseinventoryapi.dto.response.LevelResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.service.LevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/levels")
@RequiredArgsConstructor
@Tag(name = "Levels", description = "Gestión de niveles verticales en los racks de almacenamiento")
public class LevelController {

    private final LevelService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un nuevo nivel en un rack")
    public LevelResponse create(@Valid @RequestBody CreateLevelRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar los datos de un nivel existente por su ID")
    public LevelResponse update(@PathVariable Long id, @Valid @RequestBody UpdateLevelRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar la información de un nivel por su ID")
    public LevelResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un nivel de forma definitiva por su ID")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/rack/{rackId}")
    @Operation(summary = "Listar de forma paginada los niveles pertenecientes a un rack específico")
    public PageableResponse<LevelResponse> getByRackId(@PathVariable Long rackId, Pageable pageable) {
        return service.getByRackId(rackId, pageable);
    }
}