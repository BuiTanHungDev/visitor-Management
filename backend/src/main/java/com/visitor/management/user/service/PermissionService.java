package com.visitor.management.user.service;

import com.visitor.management.user.dto.CreatePermissionRequest;
import com.visitor.management.user.dto.PermissionResponse;
import com.visitor.management.user.dto.UpdatePermissionRequest;
import com.visitor.management.user.entity.Permission;
import com.visitor.management.user.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionService {

    private final PermissionRepository permissionRepository;

    // Tạo quyền hạn
    @Transactional
    public PermissionResponse createPermission(
            CreatePermissionRequest request
    ) {

        String code = normalizeCode(request.getCode());

        if (permissionRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException(
                    "Mã quyền hạn đã tồn tại"
            );
        }

        Permission permission = new Permission();

        permission.setCode(code);
        permission.setName(request.getName().trim());
        permission.setModule(
                request.getModule().trim().toUpperCase(Locale.ROOT)
        );
        permission.setDescription(
                normalizeText(request.getDescription())
        );

        return toResponse(permissionRepository.save(permission));
    }

    // Lấy quyền hạn theo ID
    public PermissionResponse getPermissionById(UUID permissionId) {

        return toResponse(findPermissionById(permissionId));
    }

    // Danh sách quyền hạn có phân trang
    public Page<PermissionResponse> getAllPermissions(
            Pageable pageable
    ) {

        return permissionRepository.findAll(pageable)
                .map(this::toResponse);
    }

    // Cập nhật thông tin quyền hạn
    @Transactional
    public PermissionResponse updatePermission(
            UUID permissionId,
            UpdatePermissionRequest request
    ) {

        Permission permission = findPermissionById(permissionId);

        permission.setName(request.getName().trim());
        permission.setModule(
                request.getModule().trim().toUpperCase(Locale.ROOT)
        );
        permission.setDescription(
                normalizeText(request.getDescription())
        );

        return toResponse(permissionRepository.save(permission));
    }

    private Permission findPermissionById(UUID permissionId) {

        return permissionRepository.findById(permissionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy quyền hạn: " + permissionId
                        )
                );
    }

    private PermissionResponse toResponse(Permission permission) {

        return PermissionResponse.builder()
                .id(permission.getId())
                .code(permission.getCode())
                .name(permission.getName())
                .module(permission.getModule())
                .description(permission.getDescription())
                .createdAt(permission.getCreatedAt())
                .build();
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeText(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}