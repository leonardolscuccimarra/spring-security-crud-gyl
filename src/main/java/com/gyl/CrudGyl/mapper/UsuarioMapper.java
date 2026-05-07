package com.gyl.CrudGyl.mapper;

import com.gyl.CrudGyl.dto.usuario.request.LoginRequestDTO;
import com.gyl.CrudGyl.entity.Role;
import com.gyl.CrudGyl.entity.Usuario;

public class UsuarioMapper {
    public UsuarioMapper() {}

    public static Usuario toEntity(LoginRequestDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setUsername(dto.username());
        usuario.setPassword(dto.password());
        usuario.setRol(Role.USER());

        return usuario;
    }
}