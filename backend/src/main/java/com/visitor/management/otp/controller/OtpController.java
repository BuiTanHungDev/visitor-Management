package com.visitor.management.otp.controller;

import com.visitor.management.otp.dto.OtpRequestDto;
import com.visitor.management.otp.dto.OtpRequestResponse;
import com.visitor.management.otp.dto.OtpVerifyRequest;
import com.visitor.management.otp.dto.OtpVerifyResponse;
import com.visitor.management.otp.enums.OtpPurpose;
import com.visitor.management.otp.service.OtpRequestService;
import com.visitor.management.otp.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpRequestService otpRequestService;
    private final OtpService otpService;

    /**
     * Yêu cầu gửi OTP phục vụ đăng ký tài khoản.
     */
    @PostMapping("/request")
    public ResponseEntity<OtpRequestResponse> requestOtp(
            @Valid @RequestBody OtpRequestDto request
    ) {
        var verificationId = otpRequestService.requestOtp(
                request.getDestination(),
                request.getChannel(),
                OtpPurpose.REGISTRATION
        );

        OtpRequestResponse response = new OtpRequestResponse(
                verificationId,
                "Mã OTP đã được gửi. Vui lòng kiểm tra email hoặc tin nhắn.",
                300
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Xác thực mã OTP.
     */
    @PostMapping("/verify")
    public ResponseEntity<OtpVerifyResponse> verifyOtp(
            @Valid @RequestBody OtpVerifyRequest request
    ) {
        boolean verified = otpService.verifyOtp(
                request.verificationId(),
                request.code()
        );

        OtpVerifyResponse response = new OtpVerifyResponse(
                request.verificationId(),
                verified,
                verified
                        ? "Xác thực OTP thành công."
                        : "Mã OTP không chính xác hoặc đã hết hạn."
        );

        return ResponseEntity.ok(response);
    }
}