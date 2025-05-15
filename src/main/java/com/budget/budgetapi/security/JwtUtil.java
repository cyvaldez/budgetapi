package com.budget.budgetapi.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    private final long jwtExpirationMs = 20*60*1000; // 24h
    private final long resetTokenExpirationMs = 15 * 60 * 1000; // 15 minutes
    private Key key;
    

    @PostConstruct
    public void init() {
        // Crée une clé compatible à partir du secret défini dans application.properties
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String email) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(jwtExpirationMs, ChronoUnit.MINUTES)))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generatePasswordResetToken(String email) {
        Instant now = Instant.now();
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(resetTokenExpirationMs, ChronoUnit.MINUTES)))
            .signWith(key,SignatureAlgorithm.HS512)
            .compact();
    }
    
    public Instant extractIssuedAt(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
                
    return claims.getIssuedAt().toInstant();
    }
   

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
