package com.example.bookapi.application.out;

import java.util.Map;

public interface TokenProvider {
    String generateToken(String subject, Map<String, Object> data, long expirationTimeMillis);

    String generateToken(Map<String, Object> data, long expirationTimeMillis);

    String renewToken(String token, long expirationTimeMillis);

    boolean validateToken(String token);

    <T> T getClaim(String token, String claimKey, Class<T> clazz);

    Map<String, ?> getClaimMap(String token);

    String getSubject(String token);
}
