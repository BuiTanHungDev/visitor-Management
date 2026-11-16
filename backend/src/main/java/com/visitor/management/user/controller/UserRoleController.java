package com.visitor.management.user.controller;

import com.visitor.management.user.dto.AddUserRolesRequest;
import com.visitor.management.user.dto.UserRoleResponse;
import com.visitor.management.user.service.UserRoleService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserRoleController {

    private final UserRoleService userRoleService;

    // Lấy danh sách vai trò của User
    @GetMapping
    public ResponseEntity<List<UserRoleResponse>> getUserRoles(
            @PathVariable UUID userId
    ) {

        return ResponseEntity.ok(
                userRoleService.getUserRoles(userId)
        );
    }

    // Gán thêm vai trò cho User
    @PostMapping
    public ResponseEntity<List<UserRoleResponse>> addUserRoles(
            @PathVariable UUID userId,
            @Valid @RequestBody AddUserRolesRequest request
    ) {

        return ResponseEntity.ok(
                userRoleService.addUserRoles(userId, request)
        );
    }

    // Thu hồi một vai trò khỏi User
    @DeleteMapping("/{roleId}")
    public ResponseEntity<List<UserRoleResponse>> removeUserRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId
    ) {

        return ResponseEntity.ok(
                userRoleService.removeUserRole(userId, roleId)
        );
    }
}