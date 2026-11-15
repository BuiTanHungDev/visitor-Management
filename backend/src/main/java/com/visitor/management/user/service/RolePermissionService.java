package com.visitor.management.user.service;

import com.visitor.management.user.entity.Permission;
import com.visitor.management.user.entity.Role;
import com.visitor.management.user.entity.RolePermission;
import com.visitor.management.user.entity.RolePermissionId;
import com.visitor.management.user.repository.PermissionRepository;
import com.visitor.management.user.repository.RolePermissionRepository;
import com.visitor.management.user.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RolePermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    // Gán quyền hạn cho vai trò
    @Transactional
    public void assignPermission(UUID roleId, UUID permissionId) {

        Role role = findRole(roleId);
        Permission permission = findPermission(permissionId);

        if (rolePermissionRepository
                .existsByRole_IdAndPermission_Id(roleId, permissionId)) {
            throw new IllegalArgumentException(
                    "Vai trò đã được gán quyền hạn này"
            );
        }

        RolePermission rolePermission = new RolePermission();

        RolePermissionId id = new RolePermissionId();
        id.setRoleId(roleId);
        id.setPermissionId(permissionId);

        rolePermission.setId(id);
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        rolePermissionRepository.save(rolePermission);
    }

    // Thu hồi quyền hạn khỏi vai trò
    @Transactional
    public void removePermission(UUID roleId, UUID permissionId) {

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy vai trò"
            );
        }

        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy quyền hạn"
            );
        }

        if (!rolePermissionRepository
                .existsByRole_IdAndPermission_Id(roleId, permissionId)) {
            throw new IllegalArgumentException(
                    "Vai trò chưa được gán quyền hạn này"
            );
        }

        rolePermissionRepository
                .deleteByRole_IdAndPermission_Id(roleId, permissionId);
    }

    // Lấy danh sách quyền hạn của vai trò
    public List<Permission> getRolePermissions(UUID roleId) {

        Role role = findRole(roleId);

        return role.getRolePermissions()
                .stream()
                .map(RolePermission::getPermission)
                .toList();
    }

    private Role findRole(UUID roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy vai trò: " + roleId
                        )
                );
    }

    private Permission findPermission(UUID permissionId) {

        return permissionRepository.findById(permissionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy quyền hạn: " + permissionId
                        )
                );
    }
}