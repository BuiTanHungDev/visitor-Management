package com.visitor.management.visitor.entity;

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
    name = "visitors",
    indexes = {
        @Index(
            name = "idx_visitors_full_name",
            columnList = "full_name"
        ),
        @Index(
            name = "idx_visitors_phone",
            columnList = "phone"
        )
    }
)
public class Visitor {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        unique = true
    )
    private User user;

    @Column(
        name = "full_name",
        nullable = false,
        length = 150
    )
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(
        name = "identity_document_type",
        length = 30
    )
    private String identityDocumentType;

    @Column(
        name = "identity_document_number",
        length = 50
    )
    private String identityDocumentNumber;

    @Column(
        name = "permanent_address",
        columnDefinition = "TEXT"
    )
    private String permanentAddress;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

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

    @OneToMany(mappedBy = "visitor")
    private Set<VisitorSoldierRelationship> relationships =
        new HashSet<>();
}