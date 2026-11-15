package com.visitor.management.user.repository;

import com.visitor.management.user.entity.UserRole;
import com.visitor.management.user.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    boolean existsByUser_IdAndRole_Id(UUID userId, UUID roleId);

    long deleteByUser_IdAndRole_Id(UUID userId, UUID roleId);
}