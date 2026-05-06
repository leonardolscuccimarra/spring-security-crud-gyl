package com.gyl.CrudGyl.dto;

public record ProductResponseDto(
        Long id,
        String nombre,
        Double precio,
        Integer stock
) {
}
