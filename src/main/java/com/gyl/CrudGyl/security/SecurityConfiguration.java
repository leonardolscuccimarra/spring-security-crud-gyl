package com.gyl.CrudGyl.security;

import com.gyl.CrudGyl.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * NOMBRE: SecurityConfiguration
 *
 * PROPOSITO: Esta clase se encarga de definir:
 * - Cómo se autentican los usuarios.
 * - Qué rutas están protegidas.
 * - Qué rutas son públicas.
 * - Qué filtro JWT se ejecuta
 * - Qué tipo de encriptación se usa para las contraseñas.
 * - Cómo Spring obtiene los usuarios desde la base de datos.
 *
 * La aplicaciòn utiliza autenticación stateless basada en JWT,
 * por lo tanto no se usan sesiones HTTP tradicionales.
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final TokenAuthFilter tokenAuthFilter;
    private final UsuarioRepository usuarioRepository;

    /**
     * NOMBRE: securityFilterChain
     *
     * PROPOSITO: Configuración principal de seguridad HTTP.
     *
     * Define:
     *  - Desactivación de CSRF.
     *  - Endpoints públicos.
     *  - Endpoints protegidos.
     *  - Políticas stateless.
     *  - Provider de autenticación.
     *  - Filtro JWT personalizado.
     *
     * @param http configuración HTTP de Spring Security
     * @return cadena de filtros de seguridad
     * @throws Exception posible exceción de configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/api/login/**","/api/register").permitAll()
                                .requestMatchers("/api/productos/**").authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /**
     *  -- Bean, encargado de manejar la autenticación --
     *
     *  NOMBRE: authenticationManeger
     *
     *  PROPOSITO: Spring lo usa internamente para:
     *  - validar usuario.
     *  - validar contraseña.
     *  - generar autenticación.
     *
     * @param config -> configuración de autenticación.
     * @return AuthenticationManeger.
     * @throws Exception posible error de configuración.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * NOMBRE: authenticationProvider
     *
     * PROPOSITO: Provider de autenticación principal
     *
     * Usa:
     * - UserDetailsService personalizado.
     * - PaswordEncoder BCrypt
     *
     * DaoAuthenticationProvider compara:
     * - contraseña enviada.
     * - contraseña encriptada almacenada.
     *
     * @return AuthenticationProvider configurado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
//        authProvider.setUserDetailsService(userDetailsService());
        //TODO: Limpiar el código comentado
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}