
package com.visitor.management.registration.entity;

import com.visitor.management.visitor.entity.Visitor;

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
    name = "registration_participants",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_registration_participant",
            columnNames = {"registration_id", "visitor_id"}
        )
    },
    indexes = {
        @Index(
            name = "idx_registration_participants_registration",
            columnList = "registration_id"
        ),
        @Index(
            name = "idx_registration_participants_visitor",
            columnList = "visitor_id"
        ),
        @Index(
            name = "idx_registration_participants_status",
            columnList = "status"
        )
    }
)
public class RegistrationParticipant {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "registration_id",
        nullable = false
    )
    private VisitRegistration registration;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "visitor_id",
        nullable = false
    )
    private Visitor visitor;

    @Column(
        name = "is_representative",
        nullable = false
    )
    private Boolean isRepresentative = false;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(name = "notes", length = 255)
    private String notes;

    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
