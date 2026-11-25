package com.visitor.management.otp.dto;

import java.util.UUID;

public record OtpRequestResponse(
        UUID verificationId,
        String message,
        long expiresInSeconds
) {
}