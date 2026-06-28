package com.example.warehouseinventoryapi.controller;

import com.example.warehouseinventoryapi.dto.request.CreateReservationRequest;
import com.example.warehouseinventoryapi.dto.response.ReservationResponse;
import com.example.warehouseinventoryapi.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Reservas de stock con vencimiento automatico")
public class ReservationController {

    private final ReservationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una reserva de stock (aparta stock, vence en 15 min)")
    public ReservationResponse create(@Valid @RequestBody CreateReservationRequest request) {
        return service.create(request);
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirmar una reserva activa (el stock sale definitivamente)")
    public ReservationResponse confirm(@PathVariable Long id) {
        return service.confirm(id);
    }

    @PostMapping("/{id}/release")
    @Operation(summary = "Liberar manualmente una reserva activa (devuelve el stock)")
    public ReservationResponse release(@PathVariable Long id) {
        return service.release(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una reserva por id")
    public ReservationResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    @Operation(summary = "Listar reservas activas")
    public List<ReservationResponse> getActive() {
        return service.getActive();
    }
}