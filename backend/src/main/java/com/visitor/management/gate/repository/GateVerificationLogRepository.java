
package com.visitor.management.gate.repository;

import com.visitor.management.gate.entity.GateVerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GateVerificationLogRepository
        extends JpaRepository<GateVerificationLog, UUID> {
}
