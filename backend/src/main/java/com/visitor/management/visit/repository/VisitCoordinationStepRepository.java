
package com.visitor.management.visit.repository;

import com.visitor.management.visit.entity.VisitCoordinationStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitCoordinationStepRepository
        extends JpaRepository<VisitCoordinationStep, UUID> {
}
