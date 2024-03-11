package com.social.api.service.iService;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface IJwtService {
    String ExtractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String GenerateRefreshToken(UserDetails userDetails);
    boolean IsTokenValid(String token, UserDetails userDetails);
    boolean validateJwtToken(String authToken);
}
