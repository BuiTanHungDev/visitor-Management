package com.visitor.management.otp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record OtpVerifyRequest(

        @NotNull(message = "Mã xác thực không được để trống")
        UUID verificationId,

        @NotBlank(message = "Mã OTP không được để trống")
        @Pattern(
                regexp = "^\\d{6}$",
                message = "Mã OTP phải gồm đúng 6 chữ số"
        )
        String code

) {
}