package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.request.CreateLocationRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLocationRequest;
import com.example.warehouseinventoryapi.dto.response.LocationResponse;
import com.example.warehouseinventoryapi.entity.Level;
import com.example.warehouseinventoryapi.entity.Location;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LocationMapper {

    public Location toEntityCreate(CreateLocationRequest request) {
        return Location.builder()
                .code(request.code())
                .occupied(false) // Al crearse, la ubicación física está vacía
                .level(Level.builder().id(request.levelId()).build())
                .build();
    }

    public Location toEntityUpdate(UpdateLocationRequest request, Long id) {
        return Location.builder()
                .id(id)
                .code(request.code())
                .occupied(request.occupied())
                .build();
    }

    public LocationResponse toDto(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getCode(),
                location.getOccupied(),
                location.getLevel() != null ? location.getLevel().getId() : null
        );
    }

    public List<LocationResponse> toDtoList(List<Location> locations) {
        return locations.stream().map(this::toDto).toList();
    }
}
