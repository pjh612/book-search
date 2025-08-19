package com.example.bookapi.infrastructure.security.jwt;

import com.example.bookapi.application.out.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JjwtProvider implements TokenProvider {
    private final SecretKey key;

    public JjwtProvider(String secretKey) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(String subject, Map<String, Object> data, long expirationTimeMillis) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(subject)
                .claims(data)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationTimeMillis))
                .signWith(key)
                .compact();
    }

    @Override
    public String generateToken(Map<String, Object> data, long expirationTimeMillis) {
        return generateToken(null, data, expirationTimeMillis);
    }

    @Override
    public String renewToken(String token, long expirationTimeMillis) {
        Claims claims = getClaims(token);

        return generateToken(claims, expirationTimeMillis);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            parseJwt(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public <T> T getClaim(String token, String claimKey, Class<T> clazz) {
        Claims claims = getClaims(token);
        return claims.get(claimKey, clazz);
    }

    @Override
    public Map<String, ?> getClaimMap(String token) {
        return getClaims(token);
    }

    @Override
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return parseJwt(token).getPayload();
    }

    private Jws<Claims> parseJwt(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}
