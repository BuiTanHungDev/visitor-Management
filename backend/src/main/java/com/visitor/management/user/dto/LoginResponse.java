
package com.visitor.management.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class LoginResponse {

    private UUID userId;

    private String username;

    private String fullName;

    private String email;

    private String status;

    private Instant lastLoginAt;

    private String accessToken;

    private String tokenType;

    private long expiresIn;
}
