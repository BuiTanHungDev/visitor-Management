
package com.visitor.management.user.repository;

import com.visitor.management.user.entity.Permission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository
        extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    List<Permission> findAllByOrderByModuleAscCodeAsc();
}
