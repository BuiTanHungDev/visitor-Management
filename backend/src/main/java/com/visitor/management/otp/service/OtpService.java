
package com.visitor.management.otp.service;

import com.visitor.management.otp.dto.OtpIssueResult;
import com.visitor.management.otp.entity.OtpVerification;
import com.visitor.management.otp.enums.OtpChannel;
import com.visitor.management.otp.enums.OtpPurpose;
import com.visitor.management.otp.repository.OtpVerificationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {

    private static final Duration OTP_EXPIRATION =
            Duration.ofMinutes(5);

    private static final Duration RESEND_COOLDOWN =
            Duration.ofSeconds(60);

    private static final Duration RATE_LIMIT_WINDOW =
            Duration.ofHours(1);

    private static final int MAX_SENDS_PER_HOUR = 5;

    private static final int MAX_ATTEMPTS = 5;

    private final OtpVerificationRepository otpRepository;

    private final OtpHashService otpHashService;

    /**
     * Tạo OTP và lưu thông tin xác thực vào database.
     *
     * Lưu ý: Mã OTP gốc chỉ được trả về cho service nội bộ
     * để thực hiện gửi OTP, không được trả trực tiếp qua API.
     */
    @Transactional
    public OtpIssueResult issueOtp(
            String destination,
            OtpChannel channel,
            OtpPurpose purpose
    ) {
        validateRequest(destination, channel, purpose);

        String normalizedDestination = normalizeDestination(
                destination,
                channel
        );

        Instant now = Instant.now();

        // Kiểm tra thời gian chờ gửi lại OTP.
        otpRepository
                .findFirstByDestinationAndPurposeOrderByCreatedAtDesc(
                        normalizedDestination,
                        purpose
                )
                .ifPresent(latestOtp -> {
                    Instant nextAllowedTime =
                            latestOtp.getSentAt().plus(RESEND_COOLDOWN);

                    if (now.isBefore(nextAllowedTime)) {
                        throw new IllegalStateException(
                                "Vui lòng chờ trước khi yêu cầu mã OTP mới"
                        );
                    }
                });

        // Giới hạn tối đa 5 lần gửi trong một giờ.
        Instant rateLimitStart = now.minus(RATE_LIMIT_WINDOW);

        long sendCount =
                otpRepository.countByDestinationAndPurposeAndSentAtAfter(
                        normalizedDestination,
                        purpose,
                        rateLimitStart
                );

        if (sendCount >= MAX_SENDS_PER_HOUR) {
            throw new IllegalStateException(
                    "Bạn đã vượt quá số lần yêu cầu OTP trong một giờ"
            );
        }

        // Tạo mã OTP 6 chữ số.
        String code = otpHashService.generateOtp();

        // Chỉ lưu mã HMAC-SHA256, không lưu mã OTP gốc.
        String codeHash = otpHashService.hash(
                code,
                purpose,
                channel,
                normalizedDestination
        );

        OtpVerification verification = new OtpVerification();

        verification.setPurpose(purpose);
        verification.setChannel(channel);
        verification.setDestination(normalizedDestination);
        verification.setCodeHash(codeHash);
        verification.setSentAt(now);
        verification.setExpiresAt(now.plus(OTP_EXPIRATION));
        verification.setAttempts(0);
        verification.setMaxAttempts(MAX_ATTEMPTS);

        OtpVerification savedVerification =
                otpRepository.save(verification);

        return new OtpIssueResult(
                savedVerification.getId(),
                code
        );
    }

    /**
     * Kiểm tra mã OTP.
     *
     * @return true nếu OTP hợp lệ, false nếu mã nhập sai.
     */
    @Transactional
    public boolean verifyOtp(
            UUID verificationId,
            String code
    ) {
        if (verificationId == null) {
            throw new IllegalArgumentException(
                    "Verification ID không được để trống"
            );
        }

        if (code == null || !code.matches("^[0-9]{6}$")) {
            return false;
        }

        OtpVerification verification = otpRepository
                .findByIdForUpdate(verificationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy yêu cầu xác thực OTP"
                ));

        Instant now = Instant.now();

        if (verification.getConsumedAt() != null) {
            throw new IllegalStateException(
                    "Mã OTP đã được sử dụng"
            );
        }

        if (verification.getVerifiedAt() != null) {
            throw new IllegalStateException(
                    "Mã OTP đã được xác thực trước đó"
            );
        }

        if (!now.isBefore(verification.getExpiresAt())) {
            throw new IllegalStateException(
                    "Mã OTP đã hết hạn"
            );
        }

        if (verification.getAttempts() >= verification.getMaxAttempts()) {
            throw new IllegalStateException(
                    "Bạn đã nhập sai OTP quá số lần cho phép"
            );
        }

        boolean matched = otpHashService.matches(
                code,
                verification.getCodeHash(),
                verification.getPurpose(),
                verification.getChannel(),
                verification.getDestination()
        );

        if (!matched) {
            verification.setAttempts(
                    verification.getAttempts() + 1
            );

            otpRepository.save(verification);

            return false;
        }

        verification.setVerifiedAt(now);

        otpRepository.save(verification);

        return true;
    }

    /**
     * Đánh dấu OTP đã được sử dụng.
     *
     * Chỉ cho phép sử dụng OTP đã xác thực,
     * chưa hết hạn và chưa được tiêu thụ.
     */
    @Transactional
    public void consumeVerifiedOtp(UUID verificationId) {

        if (verificationId == null) {
            throw new IllegalArgumentException(
                    "Verification ID không được để trống"
            );
        }

        OtpVerification verification = otpRepository
                .findByIdForUpdate(verificationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy yêu cầu xác thực OTP"
                ));

        Instant now = Instant.now();

        if (verification.getVerifiedAt() == null) {
            throw new IllegalStateException(
                    "OTP chưa được xác thực"
            );
        }

        if (verification.getConsumedAt() != null) {
            throw new IllegalStateException(
                    "OTP đã được sử dụng"
            );
        }

        if (!now.isBefore(verification.getExpiresAt())) {
            throw new IllegalStateException(
                    "Mã OTP đã hết hạn"
            );
        }

        verification.setConsumedAt(now);

        otpRepository.save(verification);
    }

    /**
     * Chuẩn hóa địa chỉ nhận OTP.
     */
    private String normalizeDestination(
            String destination,
            OtpChannel channel
    ) {
        if (channel == OtpChannel.EMAIL) {
            return destination.trim().toLowerCase();
        }

        return destination.replaceAll("[^0-9]", "");
    }

    /**
     * Kiểm tra dữ liệu yêu cầu tạo OTP.
     */
    private void validateRequest(
            String destination,
            OtpChannel channel,
            OtpPurpose purpose
    ) {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException(
                    "Địa chỉ nhận OTP không được để trống"
            );
        }

        if (channel == null) {
            throw new IllegalArgumentException(
                    "Kênh gửi OTP không được để trống"
            );
        }

        if (purpose == null) {
            throw new IllegalArgumentException(
                    "Mục đích OTP không được để trống"
            );
        }

        if (channel == OtpChannel.EMAIL) {
            String email = destination.trim();

            if (!email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )) {
                throw new IllegalArgumentException(
                        "Địa chỉ email không hợp lệ"
                );
            }
        }

        if (channel == OtpChannel.SMS) {
            String phone = destination.replaceAll("[^0-9]", "");

            if (!phone.matches("^[0-9]{8,15}$")) {
                throw new IllegalArgumentException(
                        "Số điện thoại không hợp lệ"
                );
            }
        }
    }
}
