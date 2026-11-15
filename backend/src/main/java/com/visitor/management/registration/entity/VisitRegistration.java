
package com.visitor.management.registration.entity;

import com.visitor.management.soldier.entity.Soldier;
import com.visitor.management.user.entity.User;
import com.visitor.management.visitor.entity.Visitor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import com.visitor.management.visit.entity.RegistrationApproval;
import com.visitor.management.visit.entity.VisitCoordination;
import com.visitor.management.visit.entity.VisitRegistrationStatusHistory;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "visit_registrations", uniqueConstraints = {
        @UniqueConstraint(name = "uk_visit_registrations_code", columnNames = "registration_code")
}, indexes = {
        @Index(name = "idx_visit_registrations_soldier", columnList = "soldier_id"),
        @Index(name = "idx_visit_registrations_registrant", columnList = "registrant_visitor_id"),
        @Index(name = "idx_visit_registrations_date_status", columnList = "visit_date, status"),
        @Index(name = "idx_visit_registrations_created_by", columnList = "created_by_user_id")
})
public class VisitRegistration {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "registration_code", nullable = false, length = 50)
    private String registrationCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "soldier_id", nullable = false)
    private Soldier soldier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrant_visitor_id", nullable = false)
    private Visitor registrantVisitor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    @Column(name = "registration_channel", nullable = false, length = 20)
    private String registrationChannel;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "expected_arrival_time", nullable = false)
    private LocalTime expectedArrivalTime;

    @Column(name = "expected_departure_time", nullable = false)
    private LocalTime expectedDepartureTime;

    @Column(name = "purpose", length = 500)
    private String purpose;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "qr_token", length = 100)
    private String qrToken;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "DRAFT";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationParticipant> participants = new HashSet<>();

    @OneToMany(mappedBy = "registration")
    private Set<RegistrationApproval> approvals = new HashSet<>();

    @OneToMany(mappedBy = "registration")
    private Set<VisitCoordination> coordinations = new HashSet<>();

    @OneToMany(mappedBy = "registration")
    private Set<VisitRegistrationStatusHistory> statusHistories = new HashSet<>();
}
