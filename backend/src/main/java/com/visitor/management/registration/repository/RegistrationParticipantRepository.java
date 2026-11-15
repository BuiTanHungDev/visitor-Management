
package com.visitor.management.registration.repository;

import com.visitor.management.registration.entity.RegistrationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RegistrationParticipantRepository
        extends JpaRepository<RegistrationParticipant, UUID> {
}
