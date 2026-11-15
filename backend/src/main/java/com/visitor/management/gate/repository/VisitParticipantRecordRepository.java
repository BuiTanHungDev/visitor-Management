
package com.visitor.management.gate.repository;

import com.visitor.management.visit.entity.VisitParticipantRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitParticipantRecordRepository
        extends JpaRepository<VisitParticipantRecord, UUID> {
}
