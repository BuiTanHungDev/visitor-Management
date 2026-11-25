package com.visitor.management.otp.dto;

import java.util.UUID;

public record OtpVerifyResponse(
        UUID verificationId,
        boolean verified,
        String message
) {
}