package com.visitor.management.user.service;

import com.visitor.management.user.dto.CreateRoleRequest;
import com.visitor.management.user.dto.RoleResponse;
import com.visitor.management.user.dto.UpdateRoleRequest;
import com.visitor.management.user.entity.Role;
import com.visitor.management.user.repository.RoleRepository;

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
public class RoleService {

    private final RoleRepository roleRepository;

    // Tạo vai trò
    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {

        String code = normalizeCode(request.getCode());

        if (roleRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException("Mã vai trò đã tồn tại");
        }

        Role role = new Role();

        role.setCode(code);
        role.setName(request.getName().trim());
        role.setDescription(normalizeText(request.getDescription()));
        role.setIsSystem(false);

        return toResponse(roleRepository.save(role));
    }

    // Lấy vai trò theo ID
    public RoleResponse getRoleById(UUID roleId) {

        return toResponse(findRoleById(roleId));
    }

    // Danh sách vai trò có phân trang
    public Page<RoleResponse> getAllRoles(Pageable pageable) {

        return roleRepository.findAll(pageable)
                .map(this::toResponse);
    }

    // Cập nhật thông tin vai trò
    @Transactional
    public RoleResponse updateRole(
            UUID roleId,
            UpdateRoleRequest request
    ) {

        Role role = findRoleById(roleId);

        role.setName(request.getName().trim());
        role.setDescription(normalizeText(request.getDescription()));

        return toResponse(roleRepository.save(role));
    }

    private Role findRoleById(UUID roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy vai trò: " + roleId
                        )
                );
    }

    private RoleResponse toResponse(Role role) {

        return RoleResponse.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .description(role.getDescription())
                .isSystem(role.getIsSystem())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
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