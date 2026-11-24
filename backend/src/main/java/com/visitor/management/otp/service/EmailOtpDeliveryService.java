
package com.visitor.management.otp.service;

import com.visitor.management.otp.enums.OtpChannel;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "app.otp.email",
        name = "enabled",
        havingValue = "true"
)
public class EmailOtpDeliveryService implements OtpDeliveryService {

    private final JavaMailSender mailSender;

    @Value("${app.otp.email.from}")
    private String fromEmail;

    @Override
    public boolean supports(OtpChannel channel) {
        return channel == OtpChannel.EMAIL;
    }

    @Override
    public String send(
            OtpChannel channel,
            String destination,
            String code
    ) {
        if (!supports(channel)) {
            throw new IllegalArgumentException(
                    "EmailOtpDeliveryService chỉ hỗ trợ EMAIL"
            );
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(destination);
        message.setSubject("Mã xác thực đăng ký tài khoản");

        message.setText("""
                Xin chào,

                Mã OTP xác thực đăng ký tài khoản của bạn là:

                %s

                Mã có hiệu lực trong 5 phút.

                Không chia sẻ mã này với bất kỳ ai.

                Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.

                Trân trọng,
                Hệ thống quản lý thân nhân thăm khách
                """.formatted(code));

        mailSender.send(message);

        // Email không cung cấp message ID trong implementation hiện tại.
        return null;
    }
}
