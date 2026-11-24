
package com.visitor.management.otp.service;

import com.visitor.management.otp.enums.OtpChannel;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OtpDeliveryRouter {

    private final List<OtpDeliveryService> deliveryServices;

    /**
     * Lựa chọn dịch vụ gửi OTP theo kênh EMAIL hoặc SMS.
     *
     * @param channel     Kênh gửi OTP
     * @param destination Email hoặc số điện thoại nhận OTP
     * @param code        Mã OTP cần gửi
     * @return Mã message ID từ nhà cung cấp (nếu có)
     */
    public String send(
            OtpChannel channel,
            String destination,
            String code
    ) {
        if (channel == null) {
            throw new IllegalArgumentException(
                    "Kênh gửi OTP không được để trống"
            );
        }

        OtpDeliveryService deliveryService = deliveryServices.stream()
                .filter(service -> service.supports(channel))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Chưa cấu hình dịch vụ gửi OTP cho kênh: " + channel
                ));

        return deliveryService.send(channel, destination, code);
    }
}
