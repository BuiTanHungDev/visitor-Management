
package com.visitor.management.gate.repository;

import com.visitor.management.visit.entity.UnitVisitConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnitVisitConfirmationRepository
        extends JpaRepository<UnitVisitConfirmation, UUID> {
}
