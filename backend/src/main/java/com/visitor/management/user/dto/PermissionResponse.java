package com.visitor.management.user.dto;

import com.visitor.management.user.entity.Permission;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class PermissionResponse {

    private UUID id;
    private String code;
    private String name;
    private String module;
    private String description;
    private Instant createdAt;

    public static PermissionResponse from(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .code(permission.getCode())
                .name(permission.getName())
                .module(permission.getModule())
                .description(permission.getDescription())
                .createdAt(permission.getCreatedAt())
                .build();
    }
}