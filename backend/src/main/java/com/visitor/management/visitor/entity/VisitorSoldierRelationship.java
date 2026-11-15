
package com.visitor.management.visitor.entity;

import com.visitor.management.soldier.entity.Soldier;

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
    name = "visitor_soldier_relationships",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_visitor_soldier_relationship",
            columnNames = {"visitor_id", "soldier_id"}
        )
    },
    indexes = {
        @Index(
            name = "idx_visitor_relationships_soldier",
            columnList = "soldier_id"
        ),
        @Index(
            name = "idx_visitor_relationships_visitor",
            columnList = "visitor_id"
        )
    }
)
public class VisitorSoldierRelationship {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "visitor_id",
        nullable = false
    )
    private Visitor visitor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "soldier_id",
        nullable = false
    )
    private Soldier soldier;

    @Column(
        name = "relationship_type",
        nullable = false,
        length = 30
    )
    private String relationshipType;

    @Column(name = "note", length = 255)
    private String note;

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
