package com.visitor.management.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class UserResponse {

    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String status;
    private Instant lastLoginAt;
    private Instant createdAt;
    private Instant updatedAt;
}