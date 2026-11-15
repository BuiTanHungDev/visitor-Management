
package com.visitor.management.visit.entity;

import com.visitor.management.registration.entity.VisitRegistration;
import com.visitor.management.user.entity.Role;
import com.visitor.management.user.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "registration_approvals", uniqueConstraints = {
        @UniqueConstraint(name = "uk_registration_approval_step", columnNames = {
                "registration_id",
                "workflow_round",
                "step_order"
        })
}, indexes = {
        @Index(name = "idx_registration_approvals_registration", columnList = "registration_id"),
        @Index(name = "idx_registration_approvals_assigned_role", columnList = "assigned_role_id"),
        @Index(name = "idx_registration_approvals_assigned_user", columnList = "assigned_user_id"),
        @Index(name = "idx_registration_approvals_decision", columnList = "decision")
})
public class RegistrationApproval {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_id", nullable = false)
    private VisitRegistration registration;

    @Column(name = "workflow_round", nullable = false)
    private Integer workflowRound = 1;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "step_code", nullable = false, length = 50)
    private String stepCode;

    @Column(name = "step_name", nullable = false, length = 150)
    private String stepName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_role_id")
    private Role assignedRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @Column(name = "decision", nullable = false, length = 20)
    private String decision = "PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acted_by_user_id")
    private User actedByUser;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "assigned_at", nullable = false, insertable = false, updatable = false)
    private Instant assignedAt;

    @Column(name = "acted_at")
    private Instant actedAt;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;
}
