package com.social.api.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.social.api.service.iService.IJwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import java.util.Map;

@Service
public class JwtService implements IJwtService {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);


    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    // lấy thông tin user trong token
    public String ExtractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = ExtractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // lấy toàn bộ claim(Username, secretkey, ..) từ token
    private Claims ExtractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(GetSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Tạo access token từ userDetails (Bỏ cũng đc)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // tạo access token từ extra claims và userDetails
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // extraClaims.put("role", userDetails.getAuthorities());
        // có thể thêm nhiều thông tin hơn vào phần extra claim của token
        // nếu không tìm thấy getEmail() -> define thêm bên trong accountRepository
        return BuildToken(extraClaims, userDetails, jwtExpiration);
    }

    // tạo refresh token
    public String GenerateRefreshToken(UserDetails userDetails) {
        return BuildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    // Build token từ claims, userdetails, date
    private String BuildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(GetSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // lấy secrect key đã bị mã hóa
    private Key GetSignKey() {
        System.out.println(secretKey);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // kiểm tra token có hợp lệ ko (so sánh username trong token + thời hạn của
    // token)
    public boolean IsTokenValid(String token, UserDetails userDetails) {
        final String username = ExtractUsername(token);
        return (username.equals(userDetails.getUsername()) && !IsTokenExpired(token));
    }

    private boolean IsTokenExpired(String token) {
        return ExtractExpiration(token).before(new Date(jwtExpiration));
    }

    private Date ExtractExpiration(String token) {
        return (Date) extractClaim(token, Claims::getExpiration);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }
        return false;
    }

}
