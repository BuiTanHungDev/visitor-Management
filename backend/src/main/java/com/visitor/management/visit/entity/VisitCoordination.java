
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "visit_coordinations",
    indexes = {
        @Index(
            name = "idx_visit_coordinations_registration",
            columnList = "registration_id"
        ),
        @Index(
            name = "idx_visit_coordinations_status",
            columnList = "status"
        )
    }
)
public class VisitCoordination {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_id", nullable = false)
    private VisitRegistration registration;

    @Column(name = "coordination_type", nullable = false, length = 20)
    private String coordinationType;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PLANNED";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(
        mappedBy = "coordination",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<VisitCoordinationStep> steps = new HashSet<>();
}
