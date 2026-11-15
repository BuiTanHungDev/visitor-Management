
package com.visitor.management.visit.entity;

import com.visitor.management.organization.entity.OrganizationUnit;
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
    name = "unit_visit_confirmations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_unit_visit_confirmation",
            columnNames = {"session_id", "unit_id"}
        )
    },
    indexes = {
        @Index(name = "idx_unit_confirmations_session", columnList = "session_id"),
        @Index(name = "idx_unit_confirmations_unit", columnList = "unit_id"),
        @Index(name = "idx_unit_confirmations_status", columnList = "confirmation_status")
    }
)
public class UnitVisitConfirmation {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private VisitSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private OrganizationUnit unit;

    @Column(name = "confirmation_status", nullable = false, length = 20)
    private String confirmationStatus = "PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmed_by_user_id")
    private User confirmedByUser;

    @Column(name = "confirmation_notes", columnDefinition = "TEXT")
    private String confirmationNotes;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
