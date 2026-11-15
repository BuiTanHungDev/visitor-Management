
package com.visitor.management.visit.entity;

import com.visitor.management.registration.entity.VisitRegistration;
import com.visitor.management.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "visit_sessions",
    indexes = {
        @Index(name = "idx_visit_sessions_date", columnList = "visit_date"),
        @Index(name = "idx_visit_sessions_status", columnList = "status")
    }
)
public class VisitSession {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private VisitRegistration registration;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(nullable = false, length = 30)
    private String status = "PLANNED";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opened_by_user_id")
    private User openedByUser;

    @Column(name = "opened_at")
    private Instant openedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "session")
    private Set<VisitParticipantRecord> participantRecords = new HashSet<>();

    @OneToMany(mappedBy = "session")
    private Set<UnitVisitConfirmation> unitConfirmations = new HashSet<>();
}
