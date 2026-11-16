package com.visitor.management.user.service;

import com.visitor.management.user.dto.AddRolePermissionsRequest;
import com.visitor.management.user.dto.PermissionResponse;
import com.visitor.management.user.dto.RoleResponse;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    // Lấy danh sách Role kèm Permission
    public List<RoleResponse> getAllRoles() {

        return roleRepository.findAllWithPermissions()
                .stream()
                .map(RoleResponse::from)
                .toList();
    }

    // Lấy danh sách Permission
    public List<PermissionResponse> getAllPermissions() {

        return permissionRepository.findAllByOrderByModuleAscCodeAsc()
                .stream()
                .map(PermissionResponse::from)
                .toList();
    }

    // Lấy danh sách Permission của một Role
    public List<PermissionResponse> getRolePermissions(UUID roleId) {

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy vai trò: " + roleId
            );
        }

        return rolePermissionRepository
                .findAllByRoleIdWithPermission(roleId)
                .stream()
                .map(rolePermission ->
                        PermissionResponse.from(
                                rolePermission.getPermission()
                        )
                )
                .toList();
    }

    // Thêm Permission vào Role, không xóa quyền cũ
    @Transactional
    public List<PermissionResponse> addRolePermissions(
            UUID roleId,
            AddRolePermissionsRequest request
    ) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy vai trò: " + roleId
                        )
                );

        List<UUID> permissionIds = request.getPermissionIds();

        // Kiểm tra ID trùng trong request
        Set<UUID> uniqueIds = new HashSet<>(permissionIds);

        if (uniqueIds.size() != permissionIds.size()) {
            throw new IllegalArgumentException(
                    "Danh sách quyền hạn chứa ID trùng lặp"
            );
        }

        // Kiểm tra tất cả Permission có tồn tại
        List<Permission> permissions =
                permissionRepository.findAllById(permissionIds);

        if (permissions.size() != permissionIds.size()) {

            Set<UUID> foundIds = permissions.stream()
                    .map(Permission::getId)
                    .collect(Collectors.toSet());

            List<UUID> missingIds = permissionIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new IllegalArgumentException(
                    "Không tìm thấy các quyền hạn: " + missingIds
            );
        }

        // Chỉ thêm những quyền Role chưa có
        List<RolePermission> newRolePermissions =
                permissions.stream()
                        .filter(permission ->
                                !rolePermissionRepository
                                        .existsByRole_IdAndPermission_Id(
                                                roleId,
                                                permission.getId()
                                        )
                        )
                        .map(permission -> {

                            RolePermissionId id =
                                    new RolePermissionId();

                            id.setRoleId(role.getId());
                            id.setPermissionId(permission.getId());

                            RolePermission rolePermission =
                                    new RolePermission();

                            rolePermission.setId(id);
                            rolePermission.setRole(role);
                            rolePermission.setPermission(permission);

                            return rolePermission;
                        })
                        .toList();

        rolePermissionRepository.saveAll(newRolePermissions);

        return getRolePermissions(roleId);
    }

    // Thu hồi một Permission khỏi Role
    @Transactional
    public List<PermissionResponse> removeRolePermission(
            UUID roleId,
            UUID permissionId
    ) {

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy vai trò: " + roleId
            );
        }

        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy quyền hạn: " + permissionId
            );
        }

        long deleted =
                rolePermissionRepository.deleteByRole_IdAndPermission_Id(
                        roleId,
                        permissionId
                );

        if (deleted == 0) {
            throw new IllegalArgumentException(
                    "Vai trò chưa được gán quyền hạn này"
            );
        }

        return getRolePermissions(roleId);
    }
}