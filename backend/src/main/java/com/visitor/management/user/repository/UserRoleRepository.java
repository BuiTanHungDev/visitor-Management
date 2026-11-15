
package com.visitor.management.user.repository;

import com.visitor.management.user.entity.UserRole;
import com.visitor.management.user.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
