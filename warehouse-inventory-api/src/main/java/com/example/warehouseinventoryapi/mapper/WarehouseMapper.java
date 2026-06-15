package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.request.CreateWarehouseRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateWarehouseRequest;
import com.example.warehouseinventoryapi.dto.response.WarehouseResponse;
import com.example.warehouseinventoryapi.entity.Warehouse;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class WarehouseMapper {

    public Warehouse toEntityCreate(CreateWarehouseRequest request) {
        return Warehouse.builder()
                .name(request.name())
                .address(request.address())
                .active(true) // Activo por defecto al crearse
                .build();
    }

    public Warehouse toEntityUpdate(UpdateWarehouseRequest request, Long id) {
        return Warehouse.builder()
                .id(id)
                .name(request.name())
                .address(request.address())
                .active(request.active())
                .build();
    }

    public WarehouseResponse toDto(Warehouse warehouse) {
        return new WarehouseResponse(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getAddress(),
                warehouse.getActive()
        );
    }

    public List<WarehouseResponse> toDtoList(List<Warehouse> warehouses) {
        return warehouses.stream().map(this::toDto).toList();
    }
}