package com.gyl.CrudGyl.mapper;

import com.gyl.CrudGyl.dto.usuario.request.LoginRequestDTO;
import com.gyl.CrudGyl.dto.usuario.request.RegistroRequestDTO;
import com.gyl.CrudGyl.entity.Role;
import com.gyl.CrudGyl.entity.Usuario;

public class UsuarioMapper {
    public UsuarioMapper() {}

    public static Usuario toEntity(RegistroRequestDTO dto, String password) {
        Usuario usuario = new Usuario();

        usuario.setUsername(dto.username());
        usuario.setPassword(password);
        usuario.setRol(Role.USER);

        return usuario;
    }

    public static void actualizarEntidad(Usuario usuario, LoginRequestDTO dto) {
        usuario.setUsername(dto.username());
        usuario.setPassword(dto.password());
    }
}