
package com.visitor.management.organization.repository;

import com.visitor.management.organization.entity.OrganizationUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationUnitRepository
        extends JpaRepository<OrganizationUnit, UUID> {
}
