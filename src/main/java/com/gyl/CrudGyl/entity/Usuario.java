package com.gyl.CrudGyl.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;

/**
 * NOMBRE: Usuario
 * Entidad: Representa a un Usuario dentro del sistema
 *
 * PROPOSITO: Esta clase se encarga de:
 * - Mapear la tabla "usuarios" en la base de datos.
 * - Almacenar las credenciales del usuario.
 * - Definir el rol/permisos del usuario.
 * - Integrarse con Spring Security mediante la implementación
 *      de la interfaz UserDetails
 *
 * Spring Security utiliza esta clase durante el proceso
 *      de autenticación y autorización.
 */

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role rol;

    /**
     * NOMBRE getAuthorities
     *
     * PROPOSITO: Retorna los permisos o roles del usuario.
     * - Spring Security utiliza esta información para determinar
     *      que acciones puede realizar el usuario dentro del sistema.
     *
     * - En este caso se devuelve un unico rol utilizando
     *      SimpleGrantedAuthority.
     *
     * Ejemplo:
     * ROLE_ADMIN
     * ROLE_USER
     */
    @Override
    public Collection< ? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    /**
     * NOMBRE: isAccountNonExpired
     *
     * PROPOSITO: Indica si la cuenta del usuario se encuentra expirada.
     *
     * true -> la cuenta es válida
     * false -> la cuenta expiró
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * NOMBRE: isAccountNonLocked
     *
     * PROPOSITO: Indica si la cuenta del usuario está bloqueada.
     *
     * true -> la cuenta no esta bloqueada.
     * false -> la cuenta está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * NOMBRE: isCredentialsNonExpired
     *
     * PROPOSITO: Indica si las credenciales del usuario siguen siendo válidas.
     *
     * true -> la contraseña es valida.
     * false -> la contraseña expiró.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * NOMBRE: isEnabled
     *
     * PROPOSITO: Indica si el usuario se encuentra habilitado.
     *
     * true -> usuario habilitado.
     * false -> usuario deshabilitado.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}