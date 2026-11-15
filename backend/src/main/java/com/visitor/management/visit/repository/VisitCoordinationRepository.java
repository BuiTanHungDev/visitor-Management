
package com.visitor.management.visit.repository;

import com.visitor.management.visit.entity.VisitCoordination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitCoordinationRepository
        extends JpaRepository<VisitCoordination, UUID> {
}
