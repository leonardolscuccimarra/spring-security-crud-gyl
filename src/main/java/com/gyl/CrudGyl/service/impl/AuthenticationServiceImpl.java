package com.gyl.CrudGyl.service.impl;

import com.gyl.CrudGyl.dto.usuario.request.LoginRequestDTO;
import com.gyl.CrudGyl.dto.usuario.request.RegistroRequestDTO;
import com.gyl.CrudGyl.dto.usuario.response.RegistroResponseDTO;
import com.gyl.CrudGyl.dto.usuario.response.TokenResponseDTO;
import com.gyl.CrudGyl.repository.UsuarioRepository;
import com.gyl.CrudGyl.security.TokenService;
import com.gyl.CrudGyl.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegistroResponseDTO registrar(RegistroRequestDTO dto) {
        return dto;
    }

    @Override
    public TokenResponseDTO login(LoginRequestDTO dto) {
        return new TokenResponseDTO();
    }
}