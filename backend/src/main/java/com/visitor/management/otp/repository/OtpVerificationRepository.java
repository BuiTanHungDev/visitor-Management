package com.visitor.management.otp.repository;

import com.visitor.management.otp.entity.OtpVerification;
import com.visitor.management.otp.enums.OtpPurpose;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, UUID> {

    Optional<OtpVerification>
    findFirstByDestinationAndPurposeOrderByCreatedAtDesc(
        String destination,
        OtpPurpose purpose
    );

    long countByDestinationAndPurposeAndCreatedAtAfter(
        String destination,
        OtpPurpose purpose,
        Instant createdAt
    );

    long countByDestinationAndPurposeAndSentAtAfter(
        String destination,
        OtpPurpose purpose,
        Instant sentAt
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT o
        FROM OtpVerification o
        WHERE o.id = :id
        """)
    Optional<OtpVerification> findByIdForUpdate(
        @Param("id") UUID id
    );
}