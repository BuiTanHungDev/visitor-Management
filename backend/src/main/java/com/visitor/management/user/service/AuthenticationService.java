
package com.visitor.management.user.service;

import com.visitor.management.security.service.JwtService;
import com.visitor.management.user.dto.LoginRequest;
import com.visitor.management.user.dto.LoginResponse;
import com.visitor.management.user.entity.User;
import com.visitor.management.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse authenticate(LoginRequest request) {

        String identifier = request.getIdentifier().trim();

        User user = findUserByIdentifier(identifier);

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new IllegalArgumentException(
                    "Tên đăng nhập hoặc mật khẩu không chính xác");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new IllegalArgumentException(
                    "Tài khoản hiện không hoạt động");
        }

        user.setLastLoginAt(Instant.now());

        User authenticatedUser = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(
                authenticatedUser);

        return LoginResponse.builder()
                .userId(authenticatedUser.getId())
                .username(authenticatedUser.getUsername())
                .fullName(authenticatedUser.getFullName())
                .email(authenticatedUser.getEmail())
                .status(authenticatedUser.getStatus())
                .lastLoginAt(authenticatedUser.getLastLoginAt())
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationSeconds())
                .build();
    }

    private User findUserByIdentifier(String identifier) {

        String normalizedIdentifier =
                identifier.toLowerCase(Locale.ROOT);

        return userRepository.findByUsernameIgnoreCase(identifier)
                .or(() -> userRepository.findByEmailIgnoreCase(
                        normalizedIdentifier))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tên đăng nhập hoặc mật khẩu không chính xác"));
    }
}
