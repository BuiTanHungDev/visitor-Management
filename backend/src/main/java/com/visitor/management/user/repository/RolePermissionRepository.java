
package com.visitor.management.user.repository;

import com.visitor.management.user.entity.RolePermission;
import com.visitor.management.user.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
}
