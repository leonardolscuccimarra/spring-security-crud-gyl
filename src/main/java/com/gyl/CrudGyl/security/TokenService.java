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

@Service
public class TokenService {
    private static final String SECRET_KEY = "prueba_token_gyl_2026_no_robar";

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String getToken(UserDetails user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(crearFechaDeExpiracion())
                .sign(algorithm);
    }

    private Instant crearFechaDeExpiracion() {
        return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-3:00"));
    }

    public String getUsernameFromToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token invalido o expirado");
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()));
    }
}