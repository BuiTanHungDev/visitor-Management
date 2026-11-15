package com.visitor.management.user.dto;

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
}