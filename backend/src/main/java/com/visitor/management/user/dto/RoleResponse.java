
package com.visitor.management.user.dto;

import com.visitor.management.user.entity.Role;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class RoleResponse {

    private UUID id;
    private String code;
    private String name;
    private String description;
    private Boolean isSystem;
    private List<PermissionResponse> permissions;

    public static RoleResponse from(Role role) {

        List<PermissionResponse> permissions =
                role.getRolePermissions()
                        .stream()
                        .map(rolePermission ->
                                PermissionResponse.from(
                                        rolePermission.getPermission()
                                )
                        )
                        .sorted((a, b) -> a.getCode().compareTo(b.getCode()))
                        .toList();

        return RoleResponse.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .description(role.getDescription())
                .isSystem(role.getIsSystem())
                .permissions(permissions)
                .build();
    }
}
