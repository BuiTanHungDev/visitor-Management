
package com.visitor.management.soldier.entity;

import com.visitor.management.organization.entity.OrganizationUnit;
import com.visitor.management.user.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "soldiers",
    indexes = {
        @Index(
            name = "idx_soldiers_unit_id",
            columnList = "unit_id"
        )
    }
)
public class Soldier {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        unique = true
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "unit_id",
        nullable = false
    )
    private OrganizationUnit unit;

    @Column(
        name = "service_number",
        nullable = false,
        length = 50
    )
    private String serviceNumber;

    @Column(
        name = "full_name",
        nullable = false,
        length = 150
    )
    private String fullName;

    @Column(name = "rank_name", length = 100)
    private String rankName;

    @Column(name = "position_title", length = 150)
    private String positionTitle;

    @Column(name = "joined_at")
    private LocalDate joinedAt;

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
}
