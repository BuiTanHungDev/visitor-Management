
package com.visitor.management.otp.service;

import com.visitor.management.otp.enums.OtpChannel;

public interface OtpDeliveryService {

    boolean supports(OtpChannel channel);

    String send(
            OtpChannel channel,
            String destination,
            String code
    );
}
