package com.visitor.management.user.repository;

import com.visitor.management.user.entity.RolePermission;
import com.visitor.management.user.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RolePermissionRepository
        extends JpaRepository<RolePermission, RolePermissionId> {

    boolean existsByRole_IdAndPermission_Id(
            UUID roleId,
            UUID permissionId
    );

    long deleteByRole_IdAndPermission_Id(
            UUID roleId,
            UUID permissionId
    );
}