package com.visitor.management.user.controller;

import com.visitor.management.user.dto.AddRolePermissionsRequest;
import com.visitor.management.user.dto.PermissionResponse;
import com.visitor.management.user.dto.RoleResponse;
import com.visitor.management.user.service.RoleService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // Lấy danh sách Role kèm Permission
    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {

        return ResponseEntity.ok(
                roleService.getAllRoles()
        );
    }

    // Lấy danh sách Permission
    @GetMapping("/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {

        return ResponseEntity.ok(
                roleService.getAllPermissions()
        );
    }

    // Lấy danh sách Permission của một Role
    @GetMapping("/roles/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponse>> getRolePermissions(
            @PathVariable UUID roleId
    ) {

        return ResponseEntity.ok(
                roleService.getRolePermissions(roleId)
        );
    }

    // Thêm Permission vào Role
    @PostMapping("/roles/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponse>> addRolePermissions(
            @PathVariable UUID roleId,
            @Valid @RequestBody AddRolePermissionsRequest request
    ) {

        return ResponseEntity.ok(
                roleService.addRolePermissions(roleId, request)
        );
    }

    // Thu hồi một Permission khỏi Role
    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponse>> removeRolePermission(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId
    ) {

        return ResponseEntity.ok(
                roleService.removeRolePermission(
                        roleId,
                        permissionId
                )
        );
    }
}