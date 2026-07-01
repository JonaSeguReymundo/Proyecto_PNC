package com.example.warehouseinventoryapi.dto.response;

public record LevelResponse(
        Long id,
        Integer number,
        Long rackId
) {}