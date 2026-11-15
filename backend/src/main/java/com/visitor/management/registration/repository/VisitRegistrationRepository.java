
package com.visitor.management.registration.repository;

import com.visitor.management.registration.entity.VisitRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitRegistrationRepository
        extends JpaRepository<VisitRegistration, UUID> {
}
