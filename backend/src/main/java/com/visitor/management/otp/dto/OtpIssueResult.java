
package com.visitor.management.otp.dto;

import java.util.UUID;

public record OtpIssueResult(
        UUID verificationId,
        String code
) {
}
