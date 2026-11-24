
package com.visitor.management.otp.service;

import com.fasterxml.jackson.databind.JsonNode;

import com.visitor.management.otp.enums.OtpChannel;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "app.otp.sms",
        name = "enabled",
        havingValue = "true"
)
public class SmsOtpDeliveryService implements OtpDeliveryService {

    private final RestClient.Builder restClientBuilder;

    @Value("${app.otp.sms.api-url}")
    private String apiUrl;

    @Value("${app.otp.sms.api-key}")
    private String apiKey;

    @Value("${app.otp.sms.sender}")
    private String sender;

    @Value("${app.otp.sms.template-id}")
    private String templateId;

    @Override
    public boolean supports(OtpChannel channel) {
        return channel == OtpChannel.SMS;
    }

    @Override
    public String send(
            OtpChannel channel,
            String destination,
            String code
    ) {
        if (!supports(channel)) {
            throw new IllegalArgumentException(
                    "SmsOtpDeliveryService chỉ hỗ trợ SMS"
            );
        }

        validateConfiguration();

        String phone = normalizePhone(destination);

        Map<String, Object> data = Map.of(
                "channel", "sms",
                "sender", sender,
                "phone", phone,
                "template", templateId,
                "code", code
        );

        Map<String, Object> requestBody = Map.of(
                "data", data
        );

        try {
            JsonNode response = restClientBuilder
                    .build()
                    .post()
                    .uri(apiUrl)
                    .header("X-OTP-Key", apiKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {
                throw new IllegalStateException(
                        "OTP.dev không trả về dữ liệu phản hồi"
                );
            }

            // Trả về message_id để hệ thống lưu lại thông tin gửi SMS.
            return response.path("message_id").asText(null);

        } catch (RestClientException exception) {
            throw new IllegalStateException(
                    "Không thể gửi SMS qua OTP.dev",
                    exception
            );
        }
    }

    private void validateConfiguration() {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Chưa cấu hình OTP_API_KEY"
            );
        }

        if (sender == null || sender.isBlank()) {
            throw new IllegalStateException(
                    "Chưa cấu hình OTP_SENDER"
            );
        }

        if (templateId == null || templateId.isBlank()) {
            throw new IllegalStateException(
                    "Chưa cấu hình OTP_TEMPLATE_ID"
            );
        }
    }

    private String normalizePhone(String destination) {

        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException(
                    "Số điện thoại không được để trống"
            );
        }

        String phone = destination.replaceAll("[^0-9]", "");

        if (!phone.matches("^[0-9]{8,15}$")) {
            throw new IllegalArgumentException(
                    "Số điện thoại không hợp lệ"
            );
        }

        return phone;
    }
}
