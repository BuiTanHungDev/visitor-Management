package com.visitor.management.user.dto;

import com.visitor.management.user.entity.UserRole;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class UserRoleResponse {

    private UUID roleId;
    private String roleCode;
    private String roleName;
    private Instant assignedAt;

    public static UserRoleResponse from(UserRole userRole) {

        return UserRoleResponse.builder()
                .roleId(userRole.getRole().getId())
                .roleCode(userRole.getRole().getCode())
                .roleName(userRole.getRole().getName())
                .assignedAt(userRole.getAssignedAt())
                .build();
    }
}