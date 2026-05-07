package com.gyl.CrudGyl.security;

import com.gyl.CrudGyl.service.impl.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


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
    private final UserDetailsServiceImpl userDetailsService;

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
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/api/login/**", "/api/register").permitAll()
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

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
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
