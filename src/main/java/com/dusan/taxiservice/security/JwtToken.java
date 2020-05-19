package com.dusan.taxiservice.security;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtToken {

    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private long expiration;
    
    public String createToken(UserDetails userDetails) {
        
        String role = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
        
        String username = userDetails.getUsername();
        
        String token = Jwts.builder()
                        .setSubject(username)
                        .claim("role", role)
                        .setExpiration(new Date(System.currentTimeMillis() + expiration))
                        .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                        .compact();
        
        return token;
                        
    }
    
    public boolean isValid(String token) {
        
        try {
            getParser().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private JwtParser getParser() {
        
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build();
        
    }
    
    public String getUsername(String token) {
        
        String username = getParser()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        
        return username;
            
    }
    
    public String getRole(String token) {
        
        String role = (String)getParser()
                                .parseClaimsJws(token)
                                .getBody()
                                .get("role");
        
        return role;
                
    }
}
