package com.visitor.management.otp.dto;

import com.visitor.management.otp.enums.OtpChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OtpRequestDto {

    @NotBlank(message = "Email hoặc số điện thoại không được để trống")
    private String destination;

    @NotNull(message = "Kênh nhận OTP không được để trống")
    private OtpChannel channel;
}