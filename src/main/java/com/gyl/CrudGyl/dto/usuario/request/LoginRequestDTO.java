package com.gyl.CrudGyl.dto.usuario.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "El nombre de usuario es obligatorio y no puede estar vacío")
        String username,
        @NotBlank(message = "La contraseña es obligatoria y no puede estar vacía")
        String password
) {}