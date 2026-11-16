
package com.visitor.management.security.service;

import com.visitor.management.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.expiration-ms:900000}") long expirationMs) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT Secret phải có tối thiểu 256 bit (32 byte)");
        }

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    /**
     * Tạo Access Token cho người dùng.
     */
    public String generateAccessToken(User user) {

        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationMs);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Đọc thông tin người dùng từ Token.
     */
    public UUID extractUserId(String token) {

        String subject = extractAllClaims(token).getSubject();

        return UUID.fromString(subject);
    }

    /**
     * Lấy username từ Token.
     */
    public String extractUsername(String token) {

        return extractAllClaims(token).get("username", String.class);
    }

    /**
     * Lấy JWT ID (jti) từ Token.
     */
    public UUID extractJti(String token) {

        String jti = extractAllClaims(token).getId();

        if (jti == null || jti.isBlank()) {
            throw new JwtException("JWT không có mã định danh jti");
        }

        return UUID.fromString(jti);
    }

    /**
     * Lấy thời điểm hết hạn của Token.
     */
    public Instant extractExpiration(String token) {

        Date expiration = extractAllClaims(token).getExpiration();

        return expiration.toInstant();
    }

    /**
     * Kiểm tra tính hợp lệ của Token.
     */
    public boolean isTokenValid(String token, UUID expectedUserId) {

        try {
            UUID userId = extractUserId(token);

            return userId.equals(expectedUserId);

        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    /**
     * Giải mã và xác minh chữ ký Token.
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpirationSeconds() {
        return expirationMs / 1000;
    }
}
