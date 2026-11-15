package com.visitor.management.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class RoleResponse {

    private UUID id;
    private String code;
    private String name;
    private String description;
    private Boolean isSystem;
    private Instant createdAt;
    private Instant updatedAt;
}