
package com.visitor.management.visit.repository;

import com.visitor.management.visit.entity.VisitRegistrationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitRegistrationStatusHistoryRepository
        extends JpaRepository<VisitRegistrationStatusHistory, UUID> {
}
