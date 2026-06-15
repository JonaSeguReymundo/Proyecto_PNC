package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.request.CreateLevelRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateLevelRequest;
import com.example.warehouseinventoryapi.dto.response.AisleResponse;
import com.example.warehouseinventoryapi.dto.response.LevelResponse;
import com.example.warehouseinventoryapi.entity.Aisle;
import com.example.warehouseinventoryapi.entity.Level;
import com.example.warehouseinventoryapi.entity.Rack;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LevelMapper {

    public Level toEntityCreate(CreateLevelRequest request) {
        return Level.builder()
                .number(request.number())
                .rack(Rack.builder().id(request.rackId()).build())
                .build();
    }

    public Level toEntityUpdate(UpdateLevelRequest request, Long id) {
        return Level.builder()
                .id(id)
                .number(request.number())
                .build();
    }

    public LevelResponse toDto(Level level) {
        return new LevelResponse(
                level.getId(),
                level.getNumber(),
                level.getRack() != null ? level.getRack().getId() : null
        );
    }

    public List<LevelResponse> toDtoList(List<Level> levels) {
        return levels.stream().map(this::toDto).toList();
    }

    public Page<LevelResponse> toDtoPage(Page<Level> page) {
        return page.map(this::toDto);
    }
}
