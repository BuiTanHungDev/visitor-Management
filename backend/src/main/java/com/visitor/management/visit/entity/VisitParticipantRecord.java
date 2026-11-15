
package com.visitor.management.visit.entity;

import com.visitor.management.registration.entity.RegistrationParticipant;
import com.visitor.management.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "visit_participant_records",
    indexes = {
        @Index(name = "idx_visit_participant_records_session", columnList = "session_id"),
        @Index(name = "idx_visit_participant_records_status", columnList = "status")
    }
)
public class VisitParticipantRecord {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private VisitSession session;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "registration_participant_id",
        nullable = false,
        unique = true
    )
    private RegistrationParticipant registrationParticipant;

    @Column(nullable = false, length = 20)
    private String status = "EXPECTED";

    @Column(name = "entry_at")
    private Instant entryAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_verified_by_user_id")
    private User entryVerifiedByUser;

    @Column(name = "exit_at")
    private Instant exitAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exit_verified_by_user_id")
    private User exitVerifiedByUser;

    @Column(name = "denial_reason", columnDefinition = "TEXT")
    private String denialReason;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
