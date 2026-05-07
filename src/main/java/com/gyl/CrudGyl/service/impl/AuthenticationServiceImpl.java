package com.gyl.CrudGyl.service.impl;

import com.gyl.CrudGyl.dto.usuario.request.LoginRequestDTO;
import com.gyl.CrudGyl.dto.usuario.request.RegistroRequestDTO;
import com.gyl.CrudGyl.dto.usuario.response.RegistroResponseDTO;
import com.gyl.CrudGyl.dto.usuario.response.TokenResponseDTO;
import com.gyl.CrudGyl.entity.Usuario;
import com.gyl.CrudGyl.mapper.UsuarioMapper;
import com.gyl.CrudGyl.repository.UsuarioRepository;
import com.gyl.CrudGyl.security.TokenService;
import com.gyl.CrudGyl.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * NOMBRE: AuthenticationServiceImpl
 *
 * PROPOSITO: Implementación del servicio de autenticación.
 *
 * RESPONSABILIDADES:
 * Esta clase se encarga de:
 * - Registrar nuevos usuarios.
 * - Autenticar usuarios existentes.
 * - Generar tokens JWT luego del login.
 *
 * Utiliza Spring Security para la autenticación y JWT
 * para el manejo de sesiones sin estado (stateless).
 */
@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    /**
     * ATRIBUTOS:
     * usuarioRepository: Repositorio encargado del acceso a datos de usuarios.
     * tokenService: Servicio encargado de generar y validar tokens JWT.
     * passwordEncoder: Componente utilizado para encriptar contraseñas.
     * authenticationManager: Administrador de autenticación de Spring Security.
     */
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * NOMBRE: registrar
     *
     * PROPOSITO: Registra un nuevo usuario en el sistema.
     *
     * FUNCIONAMIENTO:
     * - Encripta la contraseña recibida.
     * - Convierte el DTO en una entidad Usuario.
     * - Guarda el usuario en la base de datos.
     * - Retorna una respuesta con los datos registrados.
     *
     * @param dto Datos del usuario a registrar -> RegistroRequestDTO
     * @return DTO con la información del usuario registrado. -> RegistroResponseDTO
     */
    @Override
    public RegistroResponseDTO registrar(RegistroRequestDTO dto) {
        String password = passwordEncoder.encode(dto.password());

        Usuario usuario = UsuarioMapper.toEntity(dto, password);

        usuarioRepository.save(usuario);

        return new RegistroResponseDTO(usuario.getUsername(), usuario.getPassword());
    }

    /**
     * NOMBRE: login
     *
     * PROPOSITO: Autentica un usuario y genera un token JWT.
     *
     * FUNCIONAMIENTO:
     * - Valída username y contraseña mediante Spring Security.
     * - Busca el usuario en la base de datos.
     * - Genera un token JWT.
     * - Retorna el token al cliente.
     *
     * @param dto Credenciales del usuario. -> LoginRequestDTO
     * @return DTO que contiene el token JWT. -> TokenResponseDTO
     */
    @Override
    public TokenResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(),dto.password())
        );

        UserDetails usuario = usuarioRepository.findByUsername(dto.username())
                .orElseThrow();

        String token = tokenService.getToken(usuario);
        return new TokenResponseDTO(token);
    }
}