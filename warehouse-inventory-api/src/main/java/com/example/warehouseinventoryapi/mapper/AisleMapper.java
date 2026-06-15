package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.request.CreateAisleRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateAisleRequest;
import com.example.warehouseinventoryapi.dto.response.AisleResponse;
import com.example.warehouseinventoryapi.entity.Aisle;
import com.example.warehouseinventoryapi.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AisleMapper {

    public Aisle toEntityCreate(CreateAisleRequest request) {
        return Aisle.builder()
                .code(request.code())
                .warehouse(Warehouse.builder().id(request.warehouseId()).build())
                .build();
    }

    public Aisle toEntityUpdate(UpdateAisleRequest request, Long id) {
        return Aisle.builder()
                .id(id)
                .code(request.code())
                .build();
    }

    public AisleResponse toDto(Aisle aisle) {
        return new AisleResponse(
                aisle.getId(),
                aisle.getCode(),
                aisle.getWarehouse() != null ? aisle.getWarehouse().getId() : null
        );
    }

    public List<AisleResponse> toDtoList(List<Aisle> aisles) {
        return aisles.stream().map(this::toDto).toList();
    }

    public Page<AisleResponse> toDtoPage(Page<Aisle> page) {
        return page.map(this::toDto);
    }
}