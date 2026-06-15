package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.request.CreateRackRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateAisleRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateRackRequest;
import com.example.warehouseinventoryapi.dto.response.AisleResponse;
import com.example.warehouseinventoryapi.dto.response.RackResponse;
import com.example.warehouseinventoryapi.entity.Aisle;
import com.example.warehouseinventoryapi.entity.Rack;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RackMapper {

    public Rack toEntityCreate(CreateRackRequest request) {
        return Rack.builder()
                .code(request.code())
                .aisle(Aisle.builder().id(request.aisleId()).build())
                .build();
    }

    public Rack toEntityUpdate(UpdateRackRequest request, Long id) {
        return Rack.builder()
                .id(id)
                .code(request.code())
                .build();
    }

    public RackResponse toDto(Rack rack) {
        return new RackResponse(
                rack.getId(),
                rack.getCode(),
                rack.getAisle() != null ? rack.getAisle().getId() : null
        );
    }

    public List<RackResponse> toDtoList(List<Rack> racks) {
        return racks.stream().map(this::toDto).toList();
    }

    // Dentro de tu RackMapper.java
    public void updateEntityFromDto(UpdateRackRequest request, Rack rack) {
        if (request.code() != null) {
            rack.setCode(request.code());
        }
    }

    public Page<RackResponse> toDtoPage(Page<Rack> page) {
        return page.map(this::toDto);
    }
}
