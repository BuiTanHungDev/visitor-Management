
package com.visitor.management.visit.entity;

import com.visitor.management.registration.entity.VisitRegistration;
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
@Table(name = "visit_registration_status_histories", indexes = {
        @Index(name = "idx_registration_status_histories_registration", columnList = "registration_id"),
        @Index(name = "idx_registration_status_histories_changed_at", columnList = "changed_at")
})
public class VisitRegistrationStatusHistory {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registration_id", nullable = false)
    private VisitRegistration registration;

    @Column(name = "previous_status", length = 30)
    private String previousStatus;

    @Column(name = "new_status", nullable = false, length = 30)
    private String newStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    private User changedByUser;

    @Column(name = "change_reason", columnDefinition = "TEXT")
    private String changeReason;

    @Column(name = "changed_at", nullable = false, insertable = false, updatable = false)
    private Instant changedAt;
}
