
package com.visitor.management.visit.repository;

import com.visitor.management.visit.entity.RegistrationApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RegistrationApprovalRepository
        extends JpaRepository<RegistrationApproval, UUID> {
}
