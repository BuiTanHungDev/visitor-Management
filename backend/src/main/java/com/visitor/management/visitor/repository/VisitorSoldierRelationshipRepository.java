
package com.visitor.management.visitor.repository;

import com.visitor.management.visitor.entity.VisitorSoldierRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitorSoldierRelationshipRepository
        extends JpaRepository<VisitorSoldierRelationship, UUID> {
}
