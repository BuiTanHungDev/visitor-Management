
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
    name = "visit_coordination_steps",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_coordination_step_order",
            columnNames = {"coordination_id", "step_order"}
        )
    },
    indexes = {
        @Index(
            name = "idx_coordination_steps_coordination",
            columnList = "coordination_id"
        ),
        @Index(
            name = "idx_coordination_steps_unit",
            columnList = "unit_id"
        ),
        @Index(
            name = "idx_coordination_steps_status",
            columnList = "status"
        )
    }
)
public class VisitCoordinationStep {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coordination_id", nullable = false)
    private VisitCoordination coordination;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_id", nullable = false)
    private OrganizationUnit unit;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "responded_at")
    private Instant respondedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
