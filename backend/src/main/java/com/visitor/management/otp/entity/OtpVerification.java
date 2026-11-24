package com.visitor.management.otp.entity;

import com.visitor.management.otp.enums.OtpChannel;
import com.visitor.management.otp.enums.OtpPurpose;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "otp_verifications",
    indexes = {
        @Index(
            name = "idx_otp_destination_purpose_created",
            columnList = "destination, purpose, created_at"
        ),
        @Index(
            name = "idx_otp_expires_at",
            columnList = "expires_at"
        )
    }
)
public class OtpVerification {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false, length = 30)
    private OtpPurpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 10)
    private OtpChannel channel;

    @Column(name = "destination", nullable = false, length = 255)
    private String destination;

    @Column(name = "code_hash", nullable = false, length = 64)
    private String codeHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "attempts", nullable = false)
    private int attempts = 0;

    @Column(name = "max_attempts", nullable = false)
    private int maxAttempts = 5;

    @Column(name = "sent_at", nullable = false)
    private Instant sentAt;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "consumed_at")
    private Instant consumedAt;

    @Column(name = "provider_message_id", length = 100)
    private String providerMessageId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}