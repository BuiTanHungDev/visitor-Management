
package com.visitor.management.gate.repository;

import com.visitor.management.visit.entity.VisitSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitSessionRepository
        extends JpaRepository<VisitSession, UUID> {
}
