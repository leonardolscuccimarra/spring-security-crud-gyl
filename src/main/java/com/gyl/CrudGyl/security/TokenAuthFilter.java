package com.gyl.CrudGyl.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * NOMBRE: TokenAuthFilter
 *
 * PROPOSITO:
 * Filtro encargado de interceptar cada petición HTTP
 * para validar tokens JWT.
 *
 * RESPONSABILIDADES:
 * - Obtener el header Authorization.
 * - Verificar que el token tenga formato Bearer.
 * - Extraer el username desde el JWT.
 * - Validar el token.
 * - Autenticar al usuario en Spring Security.
 * - Registrar la autenticación en el SecurityContext.
 *
 * Este filtro se ejecuta una vez por request gracias
 * a la herencia de OncePerRequestFilter.
 *
 * FLUJO GENERAL:
 * 1. Llega una petición HTTP.
 * 2. Se busca el token JWT.
 * 3. Se valida el token.
 * 4. Se obtiene el usuario.
 * 5. Spring Security autentica automáticamente al usuario.
 * 6. La request continúa hacia el controlador.
 */
@Component
public class TokenAuthFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor del filtro JWT.
     *
     * @param tokenService servicio encargado de manejar
     *                     generación y validación de tokens JWT.
     *
     * @param userDetailsService servicio utilizado para
     *                           cargar usuarios desde la base de datos.
     */
    public TokenAuthFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * NOMBRE: doFilterInternal
     *
     * PROPOSITO:
     * Procesa cada request HTTP para verificar
     * la autenticación mediante JWT.
     *
     * FUNCIONAMIENTO:
     * - Obtiene el header Authorization.
     * - Verifica formato Bearer Token.
     * - Extrae el JWT.
     * - Obtiene el username desde el token.
     * - Busca el usuario en la base de datos.
     * - Valida el token.
     * - Crea la autenticación de Spring Security.
     * - Guarda la autenticación en el SecurityContext.
     *
     * Si el token no existe o es inválido,
     * la request continúa sin autenticación.
     *
     * @param request request HTTP entrante.
     * @param response response HTTP saliente.
     * @param filterChain cadena de filtros de Spring Security.
     *
     * @throws ServletException error relacionado al servlet.
     * @throws IOException error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        final String username = tokenService.getUsernameFromToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (tokenService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}