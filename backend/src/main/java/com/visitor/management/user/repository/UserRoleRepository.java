
package com.visitor.management.user.repository;

import com.visitor.management.user.entity.UserRole;
import com.visitor.management.user.entity.UserRoleId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository
        extends JpaRepository<UserRole, UserRoleId> {

    boolean existsByUser_IdAndRole_Id(
            UUID userId,
            UUID roleId
    );

    long deleteByUser_IdAndRole_Id(
            UUID userId,
            UUID roleId
    );

    // Đếm tổng số người dùng được gán một vai trò
    long countByRole_CodeIgnoreCase(String roleCode);

    // Đếm số người dùng ACTIVE được gán một vai trò
    long countByRole_CodeIgnoreCaseAndUser_StatusIgnoreCase(
            String roleCode,
            String status
    );

    // Lấy danh sách vai trò của người dùng
    @Query("""
            SELECT ur
            FROM UserRole ur
            JOIN FETCH ur.role
            WHERE ur.user.id = :userId
            ORDER BY ur.role.code ASC
            """)
    List<UserRole> findAllByUserIdWithRole(
            @Param("userId") UUID userId
    );
}
