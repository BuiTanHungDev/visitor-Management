
package com.visitor.management.security.service;

import com.visitor.management.security.entity.RevokedToken;
import com.visitor.management.security.repository.RevokedTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenRevocationService {

    private final JwtService jwtService;
    private final RevokedTokenRepository revokedTokenRepository;

    @Transactional
    public void revokeToken(String token) {

        UUID jti = jwtService.extractJti(token);

        // Không lưu trùng JWT đã bị thu hồi.
        if (revokedTokenRepository.existsByJti(jti)) {
            return;
        }

        Instant expiresAt = jwtService.extractExpiration(token);

        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setJti(jti);
        revokedToken.setExpiresAt(expiresAt);

        revokedTokenRepository.save(revokedToken);
    }
}
//Service này chịu trách nhiệm thu hồi JWT và lưu thông tin vào PostgreSQL.