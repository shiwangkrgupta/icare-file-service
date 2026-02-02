package com.icare.file_service.security;


import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMillis;

    public JwtUtil() {
        Dotenv dotenv = Dotenv.load();
        String keyEnv = dotenv.get("JWT_SECRET");
        String expiry = dotenv.get("JWT_EXPIRY_MINS");
        if (expiry == null) expiry = "5";
        this.key = Keys.hmacShaKeyFor(keyEnv.getBytes());
        this.expirationMillis = Long.parseLong(expiry) * 60 * 1000;
    }

    public String generateToken(Integer userId, String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .claim("uid", userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Integer getUserId(String token) {
        return parseClaims(token).get("uid", Integer.class);
    }
}
