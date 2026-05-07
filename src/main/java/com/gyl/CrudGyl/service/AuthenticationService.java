package com.gyl.CrudGyl.service;

import com.gyl.CrudGyl.dto.usuario.request.LoginRequestDTO;
import com.gyl.CrudGyl.dto.usuario.request.RegistroRequestDTO;
import com.gyl.CrudGyl.dto.usuario.response.RegistroResponseDTO;
import com.gyl.CrudGyl.dto.usuario.response.TokenResponseDTO;

public interface AuthenticationService {
    RegistroResponseDTO registrar(RegistroRequestDTO dto);

    TokenResponseDTO login(LoginRequestDTO dto);
}