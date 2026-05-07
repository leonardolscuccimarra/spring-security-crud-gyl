package com.gyl.CrudGyl.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * NOMBRE: TokenService
 *
 * PROPOSITO: Servicio encargado de la generación,
 * validación y lectura de tokens JWT.
 *
 * FUNCIONALIDAD:
 * Esta clase utiliza la librería Java JWT de Auth0 para:
 * - Generar tokens de autenticación.
 * - Validar la integridad y expiración de un token.
 * - Extraer información del token, como el username.
 *
 * El token se firma utilizando el algoritmo HMAC256 y una clave secreta.
 */
@Service
public class TokenService {
    /**
     * SECRET_KEY: Clave secreta utilizada para firmar y validar los tokens JWT.
     * algorithm: Algoritmo de encriptación utilizado para la firma del token.
     */
    private static final String SECRET_KEY = "prueba_token_gyl_2026_no_robar";
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    /**
     * NOMBRE: getToken
     *
     * PROPOSITO: Genera un token JWT para el usuario autenticado.
     *
     * El token contiene:
     * - El username del usuario como subject.
     * - La fecha de expiración del token.
     *
     * @param user Usuario autenticado -> UserDetails.
     * @return Token JWT firmado -> String.
     */
    public String getToken(UserDetails user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(crearFechaDeExpiracion())
                .sign(algorithm);
    }

    /**
     * NOMBRE: crearFechaDeExpiracion
     *
     * PROPOSITO: Genera la fecha de expiración del token.
     * (Actualmente el token tiene una duración de 15 horas
     * desde el momento de su creación.)
     *
     * @return Fecha de expiración en formato Instant.
     */
    private Instant crearFechaDeExpiracion() {
        return LocalDateTime.now().plusHours(15).toInstant(ZoneOffset.UTC);
    }

    /**
     * NOMBRE: getUserNameFromToken
     *
     * PROPOSITO: Obtiene el username almacenado dentro del token JWT.
     *
     * FUNCIONAMIENTO:
     * Primero verifica la firma y validez del token.
     * Si el token es inválido o expiró, lanza una excepción.
     *
     * @param token Token JWT -> String.
     * @return Username contenido en el token -> String.
     * @throws RuntimeException Si el token es inválido o expiró.
     */
    public String getUsernameFromToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token invalido o expirado");
        }
    }

    /**
     * NOMBRE: isToken
     * PROPOSITO: Verifica si un token es válido para un usuario determinado.
     *
     * Un token es válido cuando:
     * - El token no está expirado.
     * - La firma es correcta.
     * - El username del token coincide con el usuario autenticado.
     *
     * @param token Token JWT -> String.
     * @param userDetails Usuario autenticado -> UserDetails.
     * @return true si el token es válido, false en caso contrario -> boolean.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()));
    }
}