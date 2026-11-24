
package com.visitor.management.otp.service;

import com.visitor.management.otp.dto.OtpIssueResult;
import com.visitor.management.otp.enums.OtpChannel;
import com.visitor.management.otp.enums.OtpPurpose;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpRequestService {

    private final OtpService otpService;

    private final OtpDeliveryRouter otpDeliveryRouter;

    private final OtpProviderMessageService providerMessageService;

    /**
     * Tạo và gửi OTP qua EMAIL hoặc SMS.
     *
     * Chỉ trả về verificationId.
     * Không trả mã OTP gốc cho API client.
     */
    public UUID requestOtp(
            String destination,
            OtpChannel channel,
            OtpPurpose purpose
    ) {

        // 1. Tạo OTP và lưu thông tin vào database.
        OtpIssueResult otpIssueResult = otpService.issueOtp(
                destination,
                channel,
                purpose
        );

        // 2. Lấy ID xác thực và mã OTP nội bộ.
        UUID verificationId = otpIssueResult.verificationId();

        String code = otpIssueResult.code();

        // 3. Gửi OTP qua Router.
        String providerMessageId = otpDeliveryRouter.send(
                channel,
                destination,
                code
        );

        // 4. Lưu mã thông điệp từ nhà cung cấp nếu có.
        if (providerMessageId != null
                && !providerMessageId.isBlank()) {

            providerMessageService.updateProviderMessageId(
                    verificationId,
                    providerMessageId
            );
        }

        // 5. Chỉ trả về ID xác thực.
        return verificationId;
    }
}
