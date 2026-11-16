package com.visitor.management.user.repository;

import com.visitor.management.user.entity.RolePermission;
import com.visitor.management.user.entity.RolePermissionId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query("""
            SELECT rp
            FROM RolePermission rp
            JOIN FETCH rp.permission
            WHERE rp.role.id = :roleId
            ORDER BY rp.permission.module ASC,
                     rp.permission.code ASC
            """)
    List<RolePermission> findAllByRoleIdWithPermission(
            @Param("roleId") UUID roleId
    );
}