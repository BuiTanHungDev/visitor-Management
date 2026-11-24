
package com.visitor.management.otp.service;

import com.visitor.management.otp.enums.OtpChannel;
import com.visitor.management.otp.enums.OtpPurpose;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;

@Service
public class OtpHashService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static final int OTP_LENGTH = 6;

    private final SecureRandom secureRandom = new SecureRandom();

    private final SecretKeySpec secretKey;

    public OtpHashService(
            @Value("${app.otp.hmac-secret}") String secret) {

        byte[] secretBytes;

        try {
            secretBytes = Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                "OTP_HMAC_SECRET phải là chuỗi Base64 hợp lệ"
            );
        }

        if (secretBytes.length < 32) {
            throw new IllegalStateException(
                "OTP_HMAC_SECRET phải có tối thiểu 32 byte"
            );
        }

        this.secretKey = new SecretKeySpec(
            secretBytes,
            HMAC_ALGORITHM
        );
    }

    /**
     * Generate a cryptographically secure 6-digit OTP.
     */
    public String generateOtp() {
        int number = secureRandom.nextInt(1_000_000);

        return String.format(
            Locale.ROOT,
            "%06d",
            number
        );
    }

    /**
     * Generate HMAC-SHA256 hash.
     * Bind the OTP to its purpose, channel and destination.
     */
    public String hash(
            String otp,
            OtpPurpose purpose,
            OtpChannel channel,
            String destination) {

        String payload = String.join(
            ":",
            purpose.name(),
            channel.name(),
            destination,
            otp
        );

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKey);

            byte[] hashBytes = mac.doFinal(
                payload.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hashBytes);

        } catch (Exception exception) {
            throw new IllegalStateException(
                "Không thể tạo OTP hash",
                exception
            );
        }
    }

    /**
     * Constant-time comparison of OTP hashes.
     */
    public boolean matches(
            String otp,
            String expectedHash,
            OtpPurpose purpose,
            OtpChannel channel,
            String destination) {

        if (otp == null || expectedHash == null) {
            return false;
        }

        String actualHash = hash(
            otp,
            purpose,
            channel,
            destination
        );

        return MessageDigest.isEqual(
            actualHash.getBytes(StandardCharsets.US_ASCII),
            expectedHash.getBytes(StandardCharsets.US_ASCII)
        );
    }
}
