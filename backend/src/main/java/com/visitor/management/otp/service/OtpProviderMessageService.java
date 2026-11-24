
package com.visitor.management.otp.service;

import com.visitor.management.otp.entity.OtpVerification;
import com.visitor.management.otp.repository.OtpVerificationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpProviderMessageService {

    private final OtpVerificationRepository otpRepository;

    @Transactional
    public void updateProviderMessageId(
            UUID verificationId,
            String providerMessageId
    ) {
        if (verificationId == null
                || providerMessageId == null
                || providerMessageId.isBlank()) {
            return;
        }

        OtpVerification verification = otpRepository
                .findById(verificationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy yêu cầu xác thực OTP"
                ));

        verification.setProviderMessageId(providerMessageId);

        otpRepository.save(verification);
    }
}
