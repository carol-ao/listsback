package com.randomstuff.lists.jwt;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${security.secret-key}")
    private String secretKey;

    public String generateToken(String username) {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), "HMACSHA256");

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiração em 10 horas
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), "HMACSHA256");

        return Jwts.parser().verifyWith(key)  // Usando a chave para verificar a assinatura
                .build()
                .parseSignedClaims(token)  // Parse do token
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
