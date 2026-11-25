
package com.visitor.management.user.dto;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        String fullName,
        String email,
        String phone,
        String status,
        String message,
        Instant createdAt
) {
}
