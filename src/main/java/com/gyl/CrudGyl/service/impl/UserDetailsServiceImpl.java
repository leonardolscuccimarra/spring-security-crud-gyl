package com.gyl.CrudGyl.service.impl;

import com.gyl.CrudGyl.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * NOMBRE: UserDetailsServiceImpl
 *
 * IMPLEMENTA: UserDetailsService.
 *
 * PROPOSITO: Esta clase es utilizada por Spring Security para
 * cargar los datos de un usuario durante el proceso
 * de autenticación.
 *
 * Spring Security invoca automáticamente el método
 * loadUserByUsername() cuando un usuario intenta iniciar sesión.
 *
 * La clase obtiene el usuario desde la base de datos
 * utilizando el repositorio de usuarios.
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * usuarioRepository: Repositorio encargado del acceso a datos de usuarios.
     * */
    private final UsuarioRepository usuarioRepository;

    /**
     * NOMBRE: loadUserByUsername
     *
     * PROPOSITO: Busca un usuario por su username.
     *
     * FUNCIONAMIENTO:
     * Este método es requerido por la interfaz
     * UserDetailsService y es utilizado internamente
     * por Spring Security durante la autenticación.
     *
     * Si el usuario existe:
     * - Retorna el usuario como UserDetails.
     *
     * Si el usuario no existe:
     * - Lanza UsernameNotFoundException.
     *
     * @param username Username ingresado por el usuario -> String
     * @return Usuario encontrado -> UserDetails
     * @throws UsernameNotFoundException Si el usuario no existe.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
