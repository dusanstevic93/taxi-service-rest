package com.dusan.taxiservice.web.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public JwtToken createToken(String username, String[] roles) {
        Date expirationTime = new Date(System.currentTimeMillis() + expiration);
        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setExpiration(expirationTime)
                .signWith(key)
                .compact();

        return new JwtToken(token, expirationTime.getTime());
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String[] extractRoles(String token) {
        return Jwts.parserBuilder()
                .deserializeJsonWith(new JacksonDeserializer(Maps.of("roles", String[].class).build()))
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", String[].class);
    }
}
